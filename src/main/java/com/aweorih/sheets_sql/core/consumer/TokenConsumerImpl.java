package com.aweorih.sheets_sql.core.consumer;

import com.aweorih.sheets_sql.core.QueryRunner;
import com.aweorih.sheets_sql.core.update.SheetsUpdateVisitor;
import com.aweorih.sheets_sql.driver.SheetsDriver;
import com.aweorih.sheets_sql.google_handler.SheetsHandler;
import com.aweorih.sheets_sql.google_handler.existing.ServiceCreator;
import com.aweorih.sheets_sql.core.SheetsCreateTableVisitor;
import com.aweorih.sheets_sql.core.insert.SheetsInsertVisitor;
import com.aweorih.sheets_sql.core.select.SheetsSelectVisitor;
import com.aweorih.sheets_sql.utils.StringPair;
import com.aweorih.sheets_sql.utils.Utils;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.common.base.Verify;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TokenConsumerImpl implements TokenConsumer {

  private final ServiceCreator serviceCreator;
  private final boolean        isTestMode;
  private final QueryRunner    queryRunner;

  private Set<String> databases = new HashSet<>();
  private String      database  = null;

  public TokenConsumerImpl(
    ServiceCreator serviceCreator, QueryRunner queryRunner, boolean isTestMode
  ) {
    Verify.verifyNotNull(serviceCreator, "serviceCreator cannot be null");
    Verify.verifyNotNull(queryRunner, "queryRunner cannot be null");
    this.serviceCreator = serviceCreator;
    this.isTestMode = isTestMode;
    this.queryRunner = queryRunner;
  }

  @Override
  public List<Map<String, Object>> getTables(String schemaPattern, String schemaName) {

    SheetsHandler             sh          = serviceCreator.makeSheetsHandler();
    Spreadsheet               spreadsheet = sh.getSpreadSheetById(schemaName);
    List<Map<String, Object>> result      = new ArrayList<>();

    for (Sheet sheet : spreadsheet.getSheets()) {

      Map<String, Object> map   = new HashMap<>();
      String              title = sheet.getProperties().getTitle();

      if (title.contains(" ")) {
        title = "`" + title + "`";
      }
      result.add(map);

      map.put("TABLE_CAT", database == null ? schemaName : database);
      map.put("TABLE_NAME", title);

      map.put("SELF_REFERENCING_COL_NAME", null);
      map.put("TABLE_SCHEM", null);
      map.put("TYPE_SCHEM", null);
      map.put("TYPE_CAT", null);
      map.put("TABLE_TYPE", "TABLE");
      map.put("REMARKS", "");
      map.put("REF_GENERATION", null);
      map.put("TYPE_NAME", null);
    }

    Collections.reverse(result);

    return result;
  }

  @Override
  public Optional<List<Map<String, Object>>> consumeUseToken(String useToken) {

    if (isTestMode) return Optional.empty();

    Verify.verifyNotNull(
      useToken,
      "Tried to call 'USE' but no database value provided"
    );
    databases.add(useToken);
    database = useToken;

    return Optional.of(Collections.emptyList());
  }

  @Override
  public Optional<List<Map<String, Object>>> consumeCreateToken(
    SheetsCreateTableVisitor tableVisitor
  ) {

    if (isTestMode) return Optional.empty();

    return Optional.of(handleCreateTable(tableVisitor));
  }

  @Override
  public Optional<List<Map<String, Object>>> consumeInsertToken(SheetsInsertVisitor insertVisitor) {

    if (isTestMode) return Optional.empty();

    Optional<String> opTable = insertVisitor.getTable();
    Optional<String> opDb    = insertVisitor.getDatabase();
    Verify.verify(
      opTable.isPresent(),
      "called insert but no table provided"
    );

    Verify.verify(null != this.database || opDb.isPresent(), "no database provided");

    String id = this.database;

    if (null == this.database) {

      try {
        // calls this::setDatabase with id
        queryRunner.setDatabase(opDb.orElse(""), false);
        Verify.verifyNotNull(this.database, "no spreadsheet id provided");
        id = this.database;
      } catch (IOException e) {
        throw new RuntimeException("unknown ");
      }
    }

    id = SheetsDriver.cleanQuotationsSafe(id);

    SheetsHandler sh            = serviceCreator.makeSheetsHandler();
    Spreadsheet   spreadsheet   = sh.getSpreadSheetById(id);

    String             table         = opTable.get();
    List<List<String>> insertValues  = insertVisitor.getValues();
    List<List<Object>> executeValues = new ArrayList<>();
    insertValues.forEach(s -> executeValues.add((List) s));
    List<String> columns = insertVisitor.getColumns();
    System.out.println(columns);

    String range = "!A1";

    Map<String, List<List<Object>>> paramsWithRange = new HashMap<>();

    for (int i = 0; i < insertValues.size(); i++) {

      List<String> insertRow = insertValues.get(i);
      Verify.verify(insertRow.size() <= columns.size(), "not enough column names provided");

      for (int j = 0; j < columns.size(); j++) {

        String key = table + "!" + columns.get(j);
        String value = insertRow.get(j);

        if (!paramsWithRange.containsKey(key)) {
          paramsWithRange.put(key, new ArrayList<>());
        }

        paramsWithRange.putIfAbsent(key, new ArrayList<>());
        paramsWithRange.get(key).add(Collections.singletonList(value));
      }
    }

    if (columns.size() == 1) {
      range = "!C";
    }

    try {
      sh.push(spreadsheet, paramsWithRange);
//      sh.push(spreadsheet, executeValues, table + range);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return Optional.of(Collections.emptyList());
  }

  @Override
  public Optional<List<Map<String, Object>>> consumeSelectToken(SheetsSelectVisitor selectVisitor) {

    if (isTestMode) return Optional.empty();

    SheetsHandler sh = serviceCreator.makeSheetsHandler();

    String           localDatabase = selectVisitor.getDatabase().orElse(this.database);
    Optional<String> opTable       = selectVisitor.getTable();

    List<String>        columns            = selectVisitor.getColumns();
    Map<String, String> columnAliasMapping = selectVisitor.getColumnAliasMapping();

    Verify.verify(opTable.isPresent());
    SheetsDriver.log(1, new StringPair("db", localDatabase),
                     new StringPair("table", opTable.get()));
    Spreadsheet spreadsheet = sh.getSpreadSheetById(localDatabase);
    try {
      if (columns.isEmpty())
        return Optional.of(sh.read(localDatabase, opTable.get().replace("__________", " ")));
//        return Optional.of(sh.read(spreadsheet, opTable.get().replace("__________", " ")));
      else
        return Optional.of(readMultipleColumns(sh, spreadsheet, opTable.get(), columnAliasMapping));
    } catch (IOException e) {
      SheetsDriver.log(1, e);
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public void setDatabase(String database) {
    this.databases.add(database);
    this.database = database;
  }

  @Override
  public List<String> getDatabases() {
    return new ArrayList<>(databases);
  }

  public Optional<List<Map<String, Object>>> consumeUpdateToken(SheetsUpdateVisitor updateVisitor) {

    if (isTestMode) return Optional.empty();

    SheetsHandler sh = serviceCreator.makeSheetsHandler();

    Optional<String> opDb    = updateVisitor.getDatabase();
    Optional<String> opTable = updateVisitor.getTable();

    Verify.verify(opTable.isPresent());

    Verify.verify(null != this.database || opDb.isPresent(), "no database provided");

    String id = this.database;

    if (null == this.database) {

      try {
        // calls this::setDatabase with id
        queryRunner.setDatabase(opDb.orElse(""), false);
        Verify.verifyNotNull(this.database, "no spreadsheet id provided");
        id = this.database;
      } catch (IOException e) {
        throw new RuntimeException("unknown ");
      }
    }

    id = SheetsDriver.cleanQuotationsSafe(id);

    Spreadsheet spreadsheet = sh.getSpreadSheetById(id);

    try {
      sh.update(spreadsheet, opTable.get(), updateVisitor);
    } catch (IOException e) {
      throw new UnsupportedOperationException(e);
    }

    return Optional.empty();
  }

  private List<Map<String, Object>> readMultipleColumns(
    SheetsHandler sh,
    Spreadsheet ss,
    String table,
    Map<String, String> columnAliasMapping
  ) throws IOException {

    if (isTestMode) return Collections.emptyList();

    return sh.read(ss, table, columnAliasMapping);
  }

  private List<Map<String, Object>> handleCreateTable(SheetsCreateTableVisitor tableVisitor) {

    if (isTestMode) return Collections.emptyList();

    SheetsHandler             sh       = serviceCreator.makeSheetsHandler();
    List<Map<String, Object>> response = new ArrayList<>();
    Map<String, Object>       map      = new HashMap<>();
    response.add(map);

    String table = tableVisitor.getTable();

    try {

      String      localDatabase = this.database;
      Spreadsheet spreadsheet   = sh.getSpreadSheetById(localDatabase);
      BatchUpdateSpreadsheetResponse result = sh.generateSheet(
        spreadsheet,
        Collections.singletonList(table)
      );
      Spreadsheet updated = result.getUpdatedSpreadsheet();

      if (null == updated) {
        map.put("result", "null");
        return response;
      }

      Integer[] sheetId = {null};

      updated.getSheets().forEach(s -> {
        if (!s.getProperties().getTitle().equals(table)) return;
        sheetId[0] = s.getProperties().getSheetId();
      });

      map.put("spreadsheet_id", localDatabase);
      map.put("sheet_id", sheetId);

      return response;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}


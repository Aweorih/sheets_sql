package com.aweorih.sheets_sql.core;

import com.aweorih.sheets_sql.core.consumer.TokenConsumer;
import com.aweorih.sheets_sql.core.consumer.TokenConsumerImpl;
import com.aweorih.sheets_sql.core.insert.SheetsInsertVisitor;
import com.aweorih.sheets_sql.core.select.SheetsSelectVisitor;
import com.aweorih.sheets_sql.core.shared.ParamProvider;
import com.aweorih.sheets_sql.core.unsupported_queries.Decider;
import com.aweorih.sheets_sql.driver.SheetsDriver;
import com.aweorih.sheets_sql.driver.SheetsMetaCache;
import com.aweorih.sheets_sql.driver.connection.UrlParameter;
import com.aweorih.sheets_sql.google_handler.DriveHandler;
import com.aweorih.sheets_sql.google_handler.existing.ServiceCreator;
import com.aweorih.sheets_sql.utils.StringPair;
import com.google.common.base.Verify;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

public class QueryRunner {

  private final TokenConsumer   translator;
  private final Decider         decider;
  private final DriveHandler    driveHandler;
  private final boolean         sheetsInitialized;
  private final boolean         driveInitialized;
  private final SheetsMetaCache sheetsMetaCache;

  public QueryRunner(
    SheetsMetaCache sheetsMetaCache,
    UrlParameter urlParameter,
    TokenConsumer translator,
    boolean isTestMode
  ) {

    ServiceCreator serviceCreator = new ServiceCreator(urlParameter);
    this.sheetsMetaCache = sheetsMetaCache;
    boolean      sheetsInitialized = false;
    boolean      driveInitialized  = false;
    Decider      decider           = null;
    DriveHandler driveHandler      = null;

    if (urlParameter.isDrive()) {
      driveInitialized = true;
      if (!isTestMode)
        driveHandler = serviceCreator.makeDriveHandler();
    }

    if (urlParameter.isSheets()) {
      translator = translator == null
                   ? new TokenConsumerImpl(serviceCreator, this, isTestMode)
                   : translator;
      decider = new Decider(translator);
      sheetsInitialized = true;
    }

    this.translator = translator;
    this.sheetsInitialized = sheetsInitialized;
    this.decider = decider;
    this.driveInitialized = driveInitialized;
    this.driveHandler = driveHandler;
  }

  public void setDatabase(String database, boolean isId) throws IOException {

    Verify.verify(sheetsInitialized, "sheets has not been initialized");

    String id = database;

    if (!isId) {
      id = getSpreadSheetIdByName(database).orElse(null);
    }

    translator.setDatabase(id);
  }

  public List<Map<String, Object>> getTables(String id, String schema) throws SQLException {

    Verify.verify(sheetsInitialized, "sheets has not been initialized");
    Verify.verify(driveInitialized, "drive has not been initialized");

    return translator.getTables(id, schema);
  }

  public Optional<String> getSpreadSheetIdByName(String name) throws IOException {
    return driveHandler.getSpreadSheetIdByName(name);
  }

  public List<String> getDatabases() {

    Verify.verify(sheetsInitialized, "sheets has not been initialized");

    return translator.getDatabases();
  }

  public List<Map<String, Object>> listDatabases() throws SQLException {

    if (!driveInitialized) throw new SQLException("drive has not been initialized");

    try {
      return driveHandler.listDatabases();
    } catch (IOException e) {
      throw new SQLException(e);
    }
  }

  public List<Map<String, Object>> runQuery(String query, Object... params) throws SQLException {

    if (!sheetsInitialized) throw new SQLException("sheets has not been initialized");
    if (!driveInitialized) throw new SQLException("drive has not been initialized");
    SheetsDriver.log(11, new StringPair("query", query),
                     new StringPair("params", Arrays.asList(params).toString()));

    try {
      return internalRunQuery(query, params);
    } catch (Exception e) {
      throw new SQLException(e);
    }
  }

  private List<Map<String, Object>> internalRunQuery(String query, Object... params) {

    List<Map<String, Object>> response = new ArrayList<>();

    try {

      Optional<List<Map<String, Object>>> opResult = decider.check(query, params);

      if (opResult.isPresent()) return opResult.get();

      query = query.replace("``", "`");
      Statement              stmt          = CCJSqlParserUtil.parse(query);
      List<String>           sqlParams     = reduceParams(params);
      ParamProvider          paramProvider = new ParamProvider(sqlParams);
      SheetsStatementVisitor visitor       = new SheetsStatementVisitor(paramProvider);

      stmt.accept(visitor);

      if (visitor.isInsertVisited()) {
        SheetsInsertVisitor insertVisitor = visitor.getInsertVisitor();
        translator.consumeInsertToken(insertVisitor).ifPresent(response::addAll);

      } else if (visitor.isSelectVisited()) {
        SheetsSelectVisitor selectVisitor = visitor.getSelectVisitor();
        selectVisitor.getDatabase().ifPresent(db -> {

          try {
            getSpreadSheetIdByName(db).ifPresent(selectVisitor::setDatabase);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
        translator.consumeSelectToken(selectVisitor).ifPresent(response::addAll);
      } else if (visitor.isUseVisited()) {

        Optional<String> useStatementName = visitor.getUseStatementName();

        Verify.verify(
          useStatementName.isPresent(),
          "use called but no name provided"
        );

        translator.consumeUseToken(useStatementName.get()).ifPresent(response::addAll);
      } else if (visitor.isUpdateVisited()) {

        translator.consumeUpdateToken(visitor.getUpdateVisitor());
      }
    } catch (JSQLParserException e) {
      throw new RuntimeException(e);
    }

    return response;
  }

  public static List<String> reduceParams(Object... params) {

    List<String> response = new ArrayList<>();

    for (Object o : params) {
      if (List.class.isAssignableFrom(o.getClass())) {
        reduceList(response, (List) o);
      } else {
        response.add(o.toString());
      }
    }

    return response;
  }

  private static void reduceList(List<String> toAdd, List l) {

    for (Object o : l) {
      if (List.class.isAssignableFrom(o.getClass())) {
        reduceList(toAdd, (List) o);
      } else {
        toAdd.add(o.toString());
      }
    }
  }
}

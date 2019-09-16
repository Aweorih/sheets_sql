package com.aweorih.sheets_sql.driver;

import com.aweorih.sheets_sql.core.QueryRunner;
import com.aweorih.sheets_sql.driver.adaptors.DatabaseMetaDataAdaptor;
import com.aweorih.sheets_sql.google_handler.IOConverter;
import com.aweorih.sheets_sql.utils.Pair;
import com.aweorih.sheets_sql.utils.StringPair;
import com.aweorih.sheets_sql.utils.Utils;
import com.google.common.base.Verify;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SheetsDatabaseMetaData extends DatabaseMetaDataAdaptor {

  private static final Logger LOGGER = LogManager.getLogger(SheetsDatabaseMetaData.class);

  private final QueryRunner     queryRunner;
  private final List<String>    schemas;
  private final SheetsMetaCache sheetsMetaCache;

  private String requestedDefaultId = null;

  public SheetsDatabaseMetaData(
    SheetsMetaCache sheetsMetaCache, QueryRunner queryRunner, List<String> schemas
  ) {

    Verify.verifyNotNull(queryRunner, "queryRunner cannot be null");
    Verify.verifyNotNull(schemas, "schemas cannot be null");
    Verify.verifyNotNull(sheetsMetaCache, "sheetsMetaCache cannot be null");
    this.queryRunner = queryRunner;
    this.schemas = schemas;
    this.sheetsMetaCache = sheetsMetaCache;
  }

  @Override
  public ResultSet getTables(
    String catalog, String schemaPattern, String tableNamePattern, String[] types
  ) throws SQLException {

    List<Map<String, Object>> result = new ArrayList<>();

    List<String> schemas;

    if (schemaPattern.isEmpty() && !queryRunner.getDatabases().isEmpty()) {
      schemas = queryRunner.getDatabases();
    } else {
      schemas = Collections.singletonList(sheetsMetaCache.getSheetsIdByNameOrDefault(schemaPattern, schemaPattern));
    }
    Exception f = null;

    try {

      for (String schema : schemas) {

        result.addAll(queryRunner.getTables(schema, schema));
      }

      return new SheetsResultSet(null, result);
    } catch (SQLException e) {
      f = e;
      return null;
    } finally {
      if (null != f) SheetsDriver.log(2, f);
      SheetsDriver.log(
        2,
        new StringPair("result", Utils.writeJsonWithDefaultSettings(result)),
        new StringPair("catalog", catalog),
        new StringPair("schemaPattern", sheetsMetaCache.getSheetsIdByNameOrDefault(schemaPattern, schemaPattern)),
        new StringPair("tableNamePattern", tableNamePattern),
        new StringPair("known", queryRunner.getDatabases().toString()),
        new StringPair("types", Arrays.toString(types))
      );
    }
  }

  @Override
  public ResultSet getColumns(
    String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern
  ) throws SQLException {

    schemaPattern = sheetsMetaCache.getSheetsIdByNameOrDefault(schemaPattern, schemaPattern);

    SheetsDriver.log(
      2,
      new StringPair("catalog", catalog),
      new StringPair("cache", sheetsMetaCache.toString()),
      new StringPair("schemaPattern", schemaPattern),
      new StringPair("tableNamePattern", tableNamePattern),
      new StringPair("columnNamePattern", columnNamePattern),
      new StringPair("known", queryRunner.getDatabases().toString()),
      new StringPair("columnNamePattern", columnNamePattern)
    );

    Exception                 f          = null;
    List<Map<String, Object>> columnMeta = new ArrayList<>();
    String                    id         = null;
    try {
      List<String> schemas = Collections.singletonList(schemaPattern);

      if (schemaPattern.isEmpty() && !queryRunner.getDatabases().isEmpty()) {
        schemas = queryRunner.getDatabases();
      } else {
        queryRunner.setDatabase(schemaPattern, false);
      }

      if (tableNamePattern.contains("%")) {

        for (String schema : schemas) {
          columnMeta.addAll(parseMultipleColumns(schema, schema));
        }

      } else {
        columnMeta = parseSingleTableColumns(schemaPattern, tableNamePattern);
      }

      return new SheetsResultSet(null, columnMeta);

    } catch (Exception e) {
      f = e;
      return null;
    } finally {
      SheetsDriver.log(2, f);
      SheetsDriver.log(
        2,
        new StringPair("response", columnMeta),
        new StringPair("catalog", catalog),
        new StringPair("schemaPattern", schemaPattern),
        new StringPair("tableNamePattern", tableNamePattern),
        new StringPair("columnNamePattern", columnNamePattern),
        new StringPair("known", queryRunner.getDatabases().toString()),
        new StringPair("id", id),
        new StringPair("columnNamePattern", columnNamePattern)
      );
    }
  }

  private List<Map<String, Object>> parseMultipleColumns(String id, String schemaPattern)
    throws IOException, SQLException {

//    Map<String, String>       map      = queryRunner.getSheetIdMapping(id);
    List<Map<String, Object>> response = new ArrayList<>();
    List<Map<String, Object>> result   = queryRunner.getTables(id, schemaPattern);
//    SheetsDriver.log(new StringPair("map", map));
    SheetsDriver.log(2, new StringPair("result", result));

    for (Map<String, Object> m : result) {

      Object o = m.get("TABLE_NAME");
      if (null == o) continue;
      ;
      response.addAll(getColumnMappingForTable(schemaPattern, o.toString()));
    }

//    for (Map.Entry<String, String> entry : map.entrySet()) {
//
//      response.addAll(getColumnMappingForTable(schemaPattern, entry.getKey()));
//    }

    return response;
  }

  private List<Map<String, Object>> parseSingleTableColumns(
    String schemaPattern,
    String tableNamePattern
  ) throws SQLException {

    return getColumnMappingForTable(schemaPattern, tableNamePattern);
  }

  private List<Map<String, Object>> getColumnMappingForTable(String schema, String table)
    throws SQLException {

    List<Map<String, Object>> result = queryRunner.runQuery(
      "SELECT * FROM `" + table + "`"
    );
    List<Pair<String, Integer>> keySizeMapping = IOConverter.getColumnSizeMappingFromSelect(
      result);

    List<Map<String, Object>> columnMeta = new ArrayList<>();

    for (int i = 0; i < keySizeMapping.size(); i++) {
      Pair<String, Integer> pair = keySizeMapping.get(i);
      columnMeta.add(
        makeColumnDescription(
          schema,
          table,
          pair.left,
          pair.right,
          i
        )
      );
    }

    return columnMeta;
  }

  @Override
  public ResultSet getSchemas() throws SQLException {

    SheetsDriver.log(2);

    List<Map<String, Object>> responses = new ArrayList<>();
    for (String schema : sheetsMetaCache.getKnownNames()) {
      Map<String, Object> response = new HashMap<>();
      responses.add(response);
      response.put("table_schem", schema);
      response.put("table_catalog", schema);
    }

    return new SheetsResultSet(null, responses);

//    List<Map<String, Object>> result = new ArrayList<>();
////    try {
//    List<Map<String, Object>> maps = queryRunner
//      .listDatabases()
//      .stream()
//      .filter(map -> map.getOrDefault("webViewLink", "").toString().contains("spreadsheets"))
//      .collect(Collectors.toList());
//    int counter = 0;
//
//    for (Map<String, Object> map : maps) {
//      if (counter++ == 4) break;
//
//      Object schema = map.get("name");
//      Object id     = map.get("id");
//
//      if (null == schema || null == id) continue;
//      if (!schemas.contains(schema.toString())) continue;
//      map.put("TABLE_SCHEMA", schema);
//
//      queryRunner.setDatabase(id.toString());
//      List<Map<String, Object>> tables = queryRunner.getTables(id.toString(), schema.toString());
////        Map<String, String> tableMap = queryRunner.getSheetIdMapping(id.toString());
//
//      for (Map<String, Object> table : tables) {
//        Object oTableName = table.get("TABLE_NAME");
////        Object id        = table.get("TABLE_CAT");
//
//        if (null == oTableName) continue;
//
//        String sTableName = oTableName
//          .toString()
//          .replace(" ", "__________");
//
//        System.out.println("set database to " + id.toString() + " or " + id.toString());
//        System.out.println("requesting table '" + sTableName + "'");
//        result.addAll(
//          getColumnMappingForTable(schema.toString(), sTableName));
//      }
//    }
//
//    SheetsDriver.log(2, new StringPair("found schemas",
//                                       Utils.writeJsonWithDefaultSettings(
//                                         result)));
//
//    return new SheetsResultSet(null, result);
////    } catch (IOException e) {
////      throw new SQLException(e);
////    }
  }

  @Override
  public ResultSet getTableTypes() throws SQLException {
    SheetsDriver.log(2);

    List<Map<String, Object>> list = Arrays.asList(
      Collections.singletonMap("TABLE_TYPE", "TABLE"),
      Collections.singletonMap("TABLE_TYPE", "SYSTEM VIEW"),
      Collections.singletonMap("TABLE_TYPE", "VIEW")
    );
    return new SheetsResultSet(null, list);
  }

  @Override
  public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
    SheetsDriver.log(2, new StringPair("catalog", catalog),
                     new StringPair("schemaPattern", schemaPattern));
    return getSchemas();
  }

  private Map<String, Object> makeColumnDescription(
    String schema,
    String tableName,
    String columnName,
    int maxLength,
    int position
  ) {

    // let's do it in the same order as observed by mariaDB
    Map<String, Object> response = new HashMap<>();
//    Map<String, Object> response = new LinkedHashMap<>();

    response.put("SCOPE_TABLE", null);
    response.put("TABLE_CAT", schema);
    response.put("BUFFER_LENGTH", 65535);
    response.put("IS_NULLABLE", "YES");
    response.put("TABLE_NAME", tableName);
    response.put("COLUMN_DEF", null);
    response.put("SCOPE_CATALOG", null);
    response.put("TABLE_SCHEM", null);
    response.put("COLUMN_NAME", columnName);
    response.put("NULLABLE", 1);
    response.put("REMARKS", "");
    response.put("DECIMAL_DIGITS", null);
    response.put("NUM_PREC_RADIX", 10);
    response.put("SQL_DATETIME_SUB", 0);
    response.put("IS_GENERATEDCOLUMN", "NO");
    response.put("IS_AUTOINCREMENT", "NO");
    response.put("SQL_DATA_TYPE", 0);
    response.put("CHAR_OCTET_LENGTH", maxLength);
    response.put("ORDINAL_POSITION", position);
    response.put("SCOPE_SCHEMA", null);
    response.put("SOURCE_DATA_TYPE", null);
    response.put("DATA_TYPE", 12);
    response.put("TYPE_NAME", "VARCHAR");
    response.put("COLUMN_SIZE", maxLength);

    return response;
  }
}

package com.aweorih.sheets_sql;

import com.aweorih.sheets_sql.core.QueryRunner;
import com.aweorih.sheets_sql.driver.SheetsMetaCache;
import com.aweorih.sheets_sql.driver.connection.UrlParameter;
import com.aweorih.sheets_sql.driver.connection.UrlParameter.UrlParameterBuilder;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Samples {

  private static final boolean isDrive  = false;
  private static final boolean isSheets = true;

  private static final String applicationName     = "Quickstart";
  private static final String tokensDirectoryPath = "tokens";
  private static final String credentialsFilePath = "config/credentials.json";


  public static void main(String[] args) throws SQLException {

    Logger LOGGER = Logger.getLogger(FileDataStoreFactory.class.getName());
    LOGGER.setLevel(Level.OFF);

    UrlParameterBuilder builder = new UrlParameterBuilder()
      .withApplicationName(applicationName);
//      .withSpreadsheetId(spreadsheetsId); // can be null

    if (isDrive) {
      builder.withDriveCredentialsFilePath("")
             .withDriveTokensDirectoryPath("");
    }

    if (isSheets) {
      builder
        .withIsSheets(true)
        .withSheetsCredentialsFilePath(credentialsFilePath)
        .withSheetsTokensDirectoryPath(tokensDirectoryPath);
    }

    UrlParameter urlParameter = builder.build();

    QueryRunner queryRunner = new QueryRunner(new SheetsMetaCache(true), urlParameter, null, false);

    /*
    - set the wanted fields which will be returned when asking for meta data of the drive files
    - this is the default
    - used both in `SHOW DATABASES` and `SHOW CREATE DATABASE ?`
    - information about fields can be found here:
      https://developers.google.com/drive/api/v3/reference/files#resource
     */
    queryRunner.runQuery("SET showDatabaseFields = ?", "id, name, trashed, createdTime");

    // show the fields from `SET showDatabaseFields` for all known files
    List<Map<String, Object>> showResult = queryRunner.runQuery("SHOW DATABASES");

    for (Map<String, Object> map : showResult) {

      String id = (String) map.get("id");

      // same as `SHOW DATABASES` for a single value
      // this will be queried once as spreadsheet id and once as spreadsheet name
      List<Map<String, Object>> ct = queryRunner.runQuery("SHOW CREATE DATABASE ?", id);

      System.out.println(ct.toString());
    }

    // create a new spreadsheet
    // note: this has a default sheet
    List<Map<String, Object>> createResult = queryRunner.runQuery("CREATE Database ?", "foo");

    // singletonList
    Map<String, Object> first = createResult.get(0);

    String spreadsheetId = (String) first.get("spreadsheet_id");
    String table1        = "bar1";
    String table2        = "bar2";

    // use spreadsheet id -> otherwise we'd have to pass it in all further queries as extra param
    queryRunner.runQuery("USE ?;", spreadsheetId);

    // create new sheet
    // queryRunner.runQuery("CREATE TABLE ?.?;", spreadsheetId, table1);
    // or (no spreadsheetId required because of `USE ?`)
    queryRunner.runQuery("CREATE TABLE ?;", table1);

    // create new sheet
    queryRunner.runQuery("CREATE TABLE ?.?;", spreadsheetId, table2);
    // or (no spreadsheetId required because of `USE ?`)
    // queryRunner.runQuery("CREATE TABLE ?;", table2);

    // create 4 rows in `table1` (= bar1) with 8 written cells in total
    queryRunner.runQuery("INSERT INTO ?.? VALUES(?,?,?),(?,?,?,?),(?);", spreadsheetId, table1,
                         "10", "4", "2", 20, 100, 300, 5, "asd");
    // or (no spreadsheetId required because of `USE ?`)
    // queryRunner.runQuery("INSERT INTO ? VALUES(?,?,?),(?,?,?,?),(?);", table1, "10", "4", "2", 20, 100, 300, 5, "asd");

    // show all rows from `table1` (= bar1)
    List<Map<String, Object>> selectResponse1 = queryRunner.runQuery("Select * FROM ?.?",
                                                                     spreadsheetId, table1);
    // or
    // List<Map<String, Object>> selectResponse1 = queryRunner.runQuery("Select * FROM ?", table1);

    // create 1 row in `table2` (= bar2) with 3 written cells in total
    queryRunner.runQuery("INSERT INTO ?.? VALUES(?,?,?)", spreadsheetId, table2, "10", "4", "2");
    // or (no spreadsheetId required because of `USE ?`)
    // queryRunner.runQuery("INSERT INTO ? VALUES(?,?,?)", table2, "10", "4", "2");

    // show all rows from `table2` (= bar2)
    List<Map<String, Object>> selectResponse2 = queryRunner.runQuery("Select * FROM ?.?",
                                                                     spreadsheetId, table2);
    // or (no spreadsheetId required because of `USE ?`)
    // List<Map<String, Object>> selectResponse2 = queryRunner.runQuery("Select * FROM ?", table2);

    System.out.println(createResult);
    System.out.println(spreadsheetId);
    System.out.println(selectResponse1);
    System.out.println(selectResponse2);
  }
}

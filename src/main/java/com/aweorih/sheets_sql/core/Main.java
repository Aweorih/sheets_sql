package com.aweorih.sheets_sql.core;

import com.aweorih.sheets_sql.driver.SheetsMetaCache;
import com.aweorih.sheets_sql.driver.connection.UrlParameter;
import com.aweorih.sheets_sql.driver.connection.UrlParameter.UrlParameterBuilder;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {

  private static final String applicationName     = "Quickstart";
  private static final String tokensDirectoryPath = "tokens";
  private static final String credentialsFilePath = "config/credentials.json";

  public static void main(String[] args) throws SQLException {

    List<String> params = Arrays.asList("PARAM1", "PARAM2");
//    Statement s = CCJSqlParserUtil.parse("Select id,name, mail, plz FROM asd.foobar");
//    Statement s = CCJSqlParserUtil.parse("Use foobar");

    boolean isDrive  = false;
    boolean isSheets = true;

    UrlParameterBuilder builder = new UrlParameterBuilder()
      .withApplicationName(applicationName);
//      .withSpreadsheetId(spreadsheetsId); // can be null

    if (isDrive) {
      builder.withDriveCredentialsFilePath("")
             .withDriveTokensDirectoryPath("");
    }

    if (isSheets) {
      builder.withDriveCredentialsFilePath(credentialsFilePath)
             .withDriveTokensDirectoryPath(tokensDirectoryPath);
    }

    UrlParameter urlParameter = builder.build();

    QueryRunner queryRunner = new QueryRunner(new SheetsMetaCache(true), urlParameter, null, true);

    queryRunner.runQuery("CREATE TABLE testtab (\"test\" varchar (255))");
//    queryRunner.runQuery("INSERT INTO FOOBAR (b) VALUES(' ',10,20,30,40),(' ','aa','bb','cc','dd')");
//    List<Map<String, Object>> get = queryRunner.runQuery("SELECT a, d,b FROM FOOBAR");

//    String query = "UPDATE FOOBAR SET c1 = 43, b1 = 'hallo'";
//    String query = "UPDATE FOOBAR SET c = 42 WHERE row >= 0 and row < 3";

//    queryRunner.runQuery(query);

//    System.out.println(get);
  }
}

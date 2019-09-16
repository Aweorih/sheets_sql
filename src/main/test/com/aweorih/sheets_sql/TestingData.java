package com.aweorih.sheets_sql;

import com.aweorih.sheets_sql.driver.connection.UrlParameter;
import com.aweorih.sheets_sql.driver.connection.UrlParameter.UrlParameterBuilder;

public class TestingData {

  static final TestingValues USE_DATABASE_VALUES = new TestingValues(
    "use DATABASE ?",
    "foobar"
  );

  static final TestingValues UPDATE_MULTIPLE_COLUMNS_VALUES = new TestingValues(
    "UPDATE FOOBAR SET c1 = ?, b1 = ?",
    43, "hallo"
  );

  static final TestingValues UPDATE_WITH_ROWS_VALUES = new TestingValues(
    "UPDATE FOOBAR SET c1 = ? WHERE row >= ? and row < ?",
    42, 0, 3
  );

  static final TestingValues CREATE_TABLE_VALUES = new TestingValues(
    "CREATE TABLE ?",
    "foobar"
  );

  static final TestingValues SELECT_VALUES = new TestingValues(
    /*
    todo: verify:
      returns cell a1 and rows b and c
      in this format:
      List [
        Map{"a1"="a1", "b"="b1", "c"="c1"},
        Map{"b"="b2", "c"="c2"},
        Map{"b"="b3", "c"="c3"}
      ]
    */
    "SELECT a1,b,c FROM foobar"
  );

  static final TestingValues SINGLE_INSERT_VALUES = new TestingValues(
    "INSERT INTO foobar (a,b,c) VALUES (?,?,?)",
    1, 2, 3
  );

  static final TestingValues MULTIPLE_INSERT_VALUES = new TestingValues(
    "INSERT INTO foobar (a,b,c) VALUES (?,?,?),(?,?,?)",
    1, 2, 3, 4, 5, 6
  );

  static UrlParameter getUrlParameter() {
    return new UrlParameterBuilder()
      .withIsSheets(true)
      .withIsDrive(true)
      .withApplicationName("")
      .withSheetsCredentialsFilePath("")
      .withSheetsTokensDirectoryPath("")
      .withDriveTokensDirectoryPath("")
      .withDriveCredentialsFilePath("")
      .build();
  }

  public static final class TestingValues {

    public final String   query;
    public final Object[] params;

    public TestingValues(String query, Object... params) {
      this.query = query;
      this.params = params;
    }
  }
}

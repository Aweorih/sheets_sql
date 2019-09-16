package com.aweorih.sheets_sql.driver;

import java.sql.SQLException;
import java.util.function.Supplier;

public class ExceptionWrapper {

  public static <E> E run(Supplier<E> supplier) throws SQLException {

    try {
      return supplier.get();
    } catch (Exception e) {
      SheetsDriver.log(100, e);
      if (e.getClass() == SQLException.class) throw e;
      throw new SQLException(e);
    }
  }
}

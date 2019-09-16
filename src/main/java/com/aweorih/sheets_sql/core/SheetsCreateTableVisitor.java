package com.aweorih.sheets_sql.core;

import com.google.common.base.Verify;

public class SheetsCreateTableVisitor {

  private final String table;

  public SheetsCreateTableVisitor(String table) {
    Verify.verifyNotNull(table);
    this.table = table;
  }

  public String getTable() {
    return table;
  }
}

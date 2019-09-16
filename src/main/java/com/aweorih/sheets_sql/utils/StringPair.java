package com.aweorih.sheets_sql.utils;

public class StringPair {

  public final String left;
  public final String right;

  public StringPair(String left, String right) {
    this.left = "'" + left + "'";
    this.right = "'" + right + "'";
  }

  public StringPair(String left, Object right) {
    String r = right == null ? "null" : right.toString();
    this.left = "'" + left + "'";
    this.right = "'" + r + "'";
  }
}

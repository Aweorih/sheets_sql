package com.aweorih.sheets_sql.driver.adaptors;

import com.aweorih.sheets_sql.driver.SheetsDriver;
import com.aweorih.sheets_sql.utils.StringPair;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResultSetMetaDataAdaptor implements ResultSetMetaData {

  @Override
  public int getColumnCount() throws SQLException {
    SheetsDriver.log(9);
    return 0;
  }

  @Override
  public boolean isAutoIncrement(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return false;
  }

  @Override
  public boolean isCaseSensitive(int column) throws SQLException {
    return false;
  }

  @Override
  public boolean isSearchable(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return false;
  }

  @Override
  public boolean isCurrency(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return false;
  }

  @Override
  public int isNullable(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return 0;
  }

  @Override
  public boolean isSigned(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return false;
  }

  @Override
  public int getColumnDisplaySize(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return 0;
  }

  @Override
  public String getColumnLabel(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return null;
  }

  @Override
  public String getColumnName(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return null;
  }

  @Override
  public String getSchemaName(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return null;
  }

  @Override
  public int getPrecision(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return 0;
  }

  @Override
  public int getScale(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return 0;
  }

  @Override
  public String getTableName(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return null;
  }

  @Override
  public String getCatalogName(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return null;
  }

  @Override
  public int getColumnType(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return 0;
  }

  @Override
  public String getColumnTypeName(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return null;
  }

  @Override
  public boolean isReadOnly(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return false;
  }

  @Override
  public boolean isWritable(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return false;
  }

  @Override
  public boolean isDefinitelyWritable(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return false;
  }

  @Override
  public String getColumnClassName(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return null;
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    SheetsDriver.log(9);
    return null;
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    SheetsDriver.log(9);
    return false;
  }
}

package com.aweorih.sheets_sql.driver;

import com.aweorih.sheets_sql.driver.adaptors.ResultSetMetaDataAdaptor;
import com.aweorih.sheets_sql.utils.StringPair;
import com.google.common.base.Verify;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SheetsResultSetMetaData extends ResultSetMetaDataAdaptor {

  private static final Logger LOGGER = LogManager.getLogger(SheetsResultSetMetaData.class);

  private final List<String> keys;

  public SheetsResultSetMetaData(Map<String, Object> row) {
    Verify.verifyNotNull(row);
    this.keys = new ArrayList<>(row.keySet());
  }

  @Override
  public int getColumnCount() throws SQLException {
    int response = keys.isEmpty() ? 2 : keys.size();
    SheetsDriver.log(9, new StringPair("size", response));
    return response;
  }

  @Override
  public boolean isCaseSensitive(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return true;
  }

  @Override
  public int isNullable(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return ResultSetMetaData.columnNullable;
  }

  @Override
  public String getColumnLabel(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    if (column >= keys.size()) return "";
    return  keys.get(column - 1);
  }

  @Override
  public String getColumnName(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    if (column >= keys.size()) return "";
    return  keys.get(column - 1);
  }

  @Override
  public String getColumnTypeName(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    if (column >= keys.size()) return "";
    return  keys.get(column - 1);
  }

  @Override
  public int getColumnType(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return Types.VARCHAR;
  }

  // @todo not so important but maybe interesting how much google allows
  @Override
  public int getColumnDisplaySize(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return Integer.MAX_VALUE;
  }

  @Override
  public boolean isWritable(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return isDefinitelyWritable(column);
  }

  @Override
  public boolean isDefinitelyWritable(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return true;
  }

  @Override
  public String getColumnClassName(int column) throws SQLException {
    SheetsDriver.log(9, new StringPair("column", column));
    return String.class.getName();
  }
}

package com.aweorih.sheets_sql.driver;

import com.aweorih.sheets_sql.driver.adaptors.ResultSetAdaptor;
import com.aweorih.sheets_sql.utils.StringPair;
import com.google.common.base.Verify;
import com.google.common.base.VerifyException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("RedundantThrows")
public class SheetsResultSet extends ResultSetAdaptor {

  private static final Logger LOGGER = LogManager.getLogger(SheetsResultSet.class);

  private final List<Map<String, Object>> result;
  private final Statement                 statement;
  private final int                       maxResultSize;

  private boolean isClosed   = false;
  private int     pointer    = 0;
  private int     iterations = 0;

  public SheetsResultSet(Statement statement, List<Map<String, Object>> result) {

    this(statement, result, -1);
  }

  public SheetsResultSet(Statement statement, List<Map<String, Object>> result, int maxResultSize) {

    Verify.verifyNotNull(result);
//    Verify.verifyNotNull(statement);
    this.statement = statement;
    this.result = result;
    this.maxResultSize = maxResultSize;
  }

  private int getInternalNextPosition() {

    //@todo  changed
    int currentPosition = pointer;
//    int currentPosition = pointer + 1;
    if (maxResultSize != -1) {
      currentPosition += (iterations * maxResultSize);
    }
    return currentPosition;
  }

  @Override
  public boolean next() throws SQLException {

    /*
        int currentPosition = getInternalNextPosition();

    boolean response = ExceptionWrapper.run(() -> {

//      if (currentPosition >= result.size()) return false;
      if (currentPosition > result.size()) return false;
      if (skippedOnce && currentPosition == result.size()) return false;

      if (maxResultSize != -1 && pointer == maxResultSize) {
        pointer = 1;
        iterations++;
      }

      if (currentPosition != result.size()) pointer++;
      else skippedOnce = true;

      return true;
    });
     */

    pointer++;
// next with: response: false and pointer: 0 and size: 0 and currentPosition: 1
    int currentPosition = getInternalNextPosition();

    boolean response = pointer > result.size() ? false :  ExceptionWrapper.run(() -> {

      if (result.isEmpty()) return false;
      if (currentPosition > result.size()) return false;

      if (maxResultSize != -1 && pointer == maxResultSize) {
        pointer = 1;
        iterations++;
      }

      return true;
    });
    SheetsDriver.log( 8,
                      new StringPair("response", response),
      new StringPair("pointer", pointer),
      new StringPair("size", result.size()),
      new StringPair("currentPosition", currentPosition)
    );

    return response;
  }

  @Override
  public boolean wasNull() throws SQLException {
    SheetsDriver.log(8);
    return false;
  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException {

    SheetsDriver.log(8, new StringPair("pointer", pointer), new StringPair("size", result.size()));
    boolean mustControl = pointer == 0;

    //@todo  changed
    if (pointer >= result.size()) return new SheetsResultSetMetaData(Collections.emptyMap());
//    if (pointer >= result.size()) return null;
    if (mustControl) pointer = 1;

    ResultSetMetaData response = ExceptionWrapper.run(() -> {
      //@todo  changed
      Map<String, Object> row = getCurrentRow("getMetaData");
//      Map<String, Object> row = getCurrentRow("getMetaData", pointer);
      return new SheetsResultSetMetaData(row);
    });

    if (mustControl) pointer = 0;

//    pointer++;
    return response;
  }

  @Override
  public Statement getStatement() throws SQLException {

    SheetsDriver.log(8);

    return statement;
  }

  @Override
  public boolean isClosed() throws SQLException {
    SheetsDriver.log(8);
    return isClosed;
  }

  @Override
  public void close() throws SQLException {
    SheetsDriver.log(8);
    isClosed = true;
  }
/* ##################################
  ################# cursor position methods start #################
  ################################## */

  @Override
  public boolean isBeforeFirst() throws SQLException {
    SheetsDriver.log(8);
    return pointer < 0;
  }

  @Override
  public boolean isAfterLast() throws SQLException {
    SheetsDriver.log(8);
    //@todo  changed
    return getInternalNextPosition() > result.size();
//    return getInternalNextPosition() >= result.size();
  }


  @Override
  public boolean absolute(int row) throws SQLException {
    SheetsDriver.log( 8,
                      new StringPair("row", row)
    );

    if (Math.abs(row) >= result.size()) {
      throw new SQLException(
        String.format(
          "requested absolute is greater param size, requested: %d, existing size: %d",
          row,
          result.size()
        )
      );
    }
    pointer = (row < 0 ? result.size() : 0) + row;
    return true;
  }

  @Override
  public boolean isFirst() throws SQLException {
    SheetsDriver.log(8);
    return pointer == 0;
  }

  @Override
  public boolean isLast() throws SQLException {
    SheetsDriver.log(8);
    return getInternalNextPosition() == result.size();
  }

  @Override
  public void beforeFirst() throws SQLException {
    SheetsDriver.log(8);
    this.pointer = -1;
  }

  @Override
  public void afterLast() throws SQLException {
    SheetsDriver.log(8);
    this.pointer = result.size();
  }

  @Override
  public boolean first() throws SQLException {
    SheetsDriver.log(8);
    if (result.isEmpty()) return false;
    this.pointer = 0;
    return true;
  }

  @Override
  public boolean last() throws SQLException {
    SheetsDriver.log(8);
    if (result.isEmpty()) return false;
    this.pointer = result.size() - 1;
    return true;
  }

  @Override
  public int getRow() throws SQLException {
//    if (maxResultSize != -1 && pointer > maxResultSize) {
//      SheetsDriver.log(new StringPair("response", -1), new StringPair("maxResultSize", maxResultSize));
//      return -1;
//    }
    int response = pointer;
//    int response = pointer == -1 ? 0 : pointer;
    SheetsDriver.log(8, new StringPair("response", response),
                     new StringPair("maxResultSize", maxResultSize));
    return response;
  }

  /* ##################################
  ################# cursor position methods end #################
  ################################## */

   /* ##################################
  ################# get methods start #################
  ################################## */

//  private String getStringSilent(String columnLabel) {
//
//    SheetsDriver.log(new StringPair("columnLabel", columnLabel));
//
//    Map<String, Object> currentRow = getCurrentRow("getString");
//    Object              value      = currentRow.get(columnLabel);
//
//    return value.toString();
//  }
//
//
//  private String getStringSilent(int columnIndex) {
//
//    SheetsDriver.log(new StringPair("columnIndex", columnIndex));
//
//    Map<String, Object> currentRow = getCurrentRow("getString");
//    List<Object>        values     = new ArrayList<>(currentRow.values());
//    Object              value      = values.get(columnIndex - 1);
//
//    return null == value ? null : value.toString();
//  }

  private Object getObjectSilent(String columnLabel) {

    Map<String, Object> currentRow = getCurrentRow("getString");

    Object o = currentRow.get(columnLabel);

    SheetsDriver.log(8, new StringPair("columnLabel", columnLabel), new StringPair("response", o));
    return o;
  }

  private Object getObjectSilent(int columnIndex) {

    Map<String, Object> currentRow = getCurrentRow("getString");
    List<Object>        values     = new ArrayList<>(currentRow.values());
    Object              value      = values.get(columnIndex - 1);

    SheetsDriver.log(8, new StringPair("columnIndex", columnIndex), new StringPair("response", value));

    return value;
  }

  //result
  @Override
  public Object getObject(String columnLabel) throws SQLException {
    return getObjectSilent(columnLabel);
  }

  @Override
  public String getString(String columnLabel) throws SQLException {
    Object o = getObjectSilent(columnLabel);
    if (null == o) return null;
    return o.toString();
  }

  @Override
  public boolean getBoolean(String columnLabel) throws SQLException {

    return ExceptionWrapper.run(() -> {
      Object o = getObjectSilent(columnLabel);
      if (null == o) return null;
      String value = o.toString();
      return Boolean.parseBoolean(value);
    });
  }

  @Override
  public String getString(int columnIndex) throws SQLException {

    return ExceptionWrapper.run(() -> {

      Object o = getObjectSilent(columnIndex);
      if (null == o) return null;
      return o.toString();
    });
  }

  @Override
  public boolean getBoolean(int columnIndex) throws SQLException {

    return ExceptionWrapper.run(() -> {
      Object o = getObjectSilent(columnIndex);
      if (null == o) return null;
      String value = o.toString();
      return Boolean.parseBoolean(value);
    });
  }

  @Override
  public byte getByte(int columnIndex) throws SQLException {

    return ExceptionWrapper.run(() -> {
      Object o = getObjectSilent(columnIndex);
      if (null == o) return null;
      String value = o.toString();
      return Byte.parseByte(value);
    });
  }

  @Override
  public short getShort(int columnIndex) throws SQLException {

    return ExceptionWrapper.run(() -> {
      Object o = getObjectSilent(columnIndex);
      if (null == o) return null;
      String value = o.toString();
      return Short.parseShort(value);
    });
  }

  @Override
  public int getInt(int columnIndex) throws SQLException {
    return ExceptionWrapper.run(() -> {
      Object o = getObjectSilent(columnIndex);
      if (null == o) return null;
      String value = o.toString();
      return Integer.parseInt(value);
    });
  }

  @Override
  public long getLong(int columnIndex) throws SQLException {
    return ExceptionWrapper.run(() -> {
      Object o = getObjectSilent(columnIndex);
      if (null == o) return null;
      String value = o.toString();
      return Long.parseLong(value);
    });
  }

  @Override
  public float getFloat(int columnIndex) throws SQLException {
    return ExceptionWrapper.run(() -> {
      Object o = getObjectSilent(columnIndex);
      if (null == o) return null;
      String value = o.toString();
      return Float.parseFloat(value);
    });
  }

  @Override
  public double getDouble(int columnIndex) throws SQLException {
    return ExceptionWrapper.run(() -> {
      Object o = getObjectSilent(columnIndex);
      if (null == o) return null;
      String value = o.toString();
      return Double.parseDouble(value);
    });
  }

  @Override
  public byte[] getBytes(int columnIndex) throws SQLException {
    return ExceptionWrapper.run(() -> {
//      String value = getObjectSilent(columnIndex).toString();
//      Verify.verifyNotNull(value);
      return new byte[0];
    });
  }

  @Override
  public Date getDate(int columnIndex) throws SQLException {
    return ExceptionWrapper.run(() -> {
      Object o = getObjectSilent(columnIndex);
      if (null == o) return null;
      String value = o.toString();
      return Date.valueOf(value);
    });
  }

  @Override
  public Time getTime(int columnIndex) throws SQLException {
    return ExceptionWrapper.run(() -> {
      Object o = getObjectSilent(columnIndex);
      if (null == o) return null;
      String value = o.toString();
      return Time.valueOf(value);
    });
  }

  @Override
  public Timestamp getTimestamp(int columnIndex) throws SQLException {
    return ExceptionWrapper.run(() -> {
      Object o = getObjectSilent(columnIndex);
      if (null == o) return null;
      String value = o.toString();
      return Timestamp.valueOf(value);
    });
  }

  @Override
  public Object getObject(int columnIndex) throws SQLException {
    return ExceptionWrapper.run(() -> getObjectSilent(columnIndex));
  }

  @Override
  public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
    return ExceptionWrapper.run(() -> {
      Object o = getObjectSilent(columnIndex);
      if (null == o) return null;
      String value = o.toString();
      return new BigDecimal(value);
    });
  }

  /* get by label/AS (SELECT foo AS bar) */

   /* ##################################
  ################# get methods end #################
  ################################## */

  private Map<String, Object> getCurrentRow(String caller) {

    return getCurrentRow(caller, pointer);
  }

  private Map<String, Object> getCurrentRow(String caller, int pointer) {

    verifyPointerPositionIsValidForGet(caller, pointer);
    if (result.isEmpty()) return Collections.emptyMap();
    return result.get(pointer - 1);
  }

  private void verifyPointerPositionIsValidForGet(String caller, int pointer) {

    Verify.verify(pointer >= 0, "%s cannot be called when pointer is below 0, pointer is: %s",
                  caller, pointer);
    Verify.verify(result.isEmpty() || getInternalNextPosition() < result.size() + 1,
                  "pointer is after result set size, pointer is: %s",
                  getInternalNextPosition());
  }
}

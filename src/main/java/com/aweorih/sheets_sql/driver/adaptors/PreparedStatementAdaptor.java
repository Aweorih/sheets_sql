package com.aweorih.sheets_sql.driver.adaptors;

import com.aweorih.sheets_sql.driver.SheetsDriver;
import com.aweorih.sheets_sql.utils.StringPair;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreparedStatementAdaptor implements java.sql.PreparedStatement {

  @Override
  public ResultSet executeQuery() throws SQLException {
    return null;
  }

  @Override
  public int executeUpdate() throws SQLException {
    return 0;
  }

  @Override
  public void setNull(int parameterIndex, int sqlType) throws SQLException {

  }

  @Override
  public void setBoolean(int parameterIndex, boolean x) throws SQLException {

  }

  @Override
  public void setByte(int parameterIndex, byte x) throws SQLException {

  }

  @Override
  public void setShort(int parameterIndex, short x) throws SQLException {

  }

  @Override
  public void setInt(int parameterIndex, int x) throws SQLException {

  }

  @Override
  public void setLong(int parameterIndex, long x) throws SQLException {

  }

  @Override
  public void setFloat(int parameterIndex, float x) throws SQLException {

  }

  @Override
  public void setDouble(int parameterIndex, double x) throws SQLException {

  }

  @Override
  public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {

  }

  @Override
  public void setString(int parameterIndex, String x) throws SQLException {

  }

  @Override
  public void setBytes(int parameterIndex, byte[] x) throws SQLException {

  }

  @Override
  public void setDate(int parameterIndex, Date x) throws SQLException {

  }

  @Override
  public void setTime(int parameterIndex, Time x) throws SQLException {

  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {

  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {

  }

  @Override
  public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {

  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {

  }

  @Override
  public void clearParameters() throws SQLException {

  }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {

  }

  @Override
  public void setObject(int parameterIndex, Object x) throws SQLException {

  }

  @Override
  public boolean execute() throws SQLException {
    return false;
  }

  @Override
  public void addBatch() throws SQLException {

  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, int length)
    throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("reader", reader),
      new StringPair("length", length)
    );
  }

  @Override
  public void setRef(int parameterIndex, Ref x) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("x", x)
    );
  }

  @Override
  public void setBlob(int parameterIndex, Blob x) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("x", x)
    );
  }

  @Override
  public void setClob(int parameterIndex, Clob x) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("x", x)
    );
  }

  @Override
  public void setArray(int parameterIndex, Array x) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("x", x)
    );
  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException {
    return null;
  }

  @Override
  public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("x", x),
      new StringPair("cal", cal)
    );
  }

  @Override
  public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("x", x),
      new StringPair("cal", cal)
    );
  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("x", x),
      new StringPair("cal", cal)
    );
  }

  @Override
  public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("sqlType", sqlType),
      new StringPair("typeName", typeName)
    );
  }

  @Override
  public void setURL(int parameterIndex, URL x) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("x", x)
    );
  }

  @Override
  public ParameterMetaData getParameterMetaData() throws SQLException {
    return null;
  }

  @Override
  public void setRowId(int parameterIndex, RowId x) throws SQLException {

  }

  @Override
  public void setNString(int parameterIndex, String value) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("value", value)
    );
  }

  @Override
  public void setNCharacterStream(int parameterIndex, Reader value, long length)
    throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("value", value),
      new StringPair("length", length)
    );
  }

  @Override
  public void setNClob(int parameterIndex, NClob value) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("value", value)
    );
  }

  @Override
  public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("reader", reader),
      new StringPair("length", length)
    );
  }

  @Override
  public void setBlob(int parameterIndex, InputStream inputStream, long length)
    throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("inputStream", inputStream),
      new StringPair("length", length)
    );
  }

  @Override
  public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("reader", reader),
      new StringPair("length", length)
    );
  }

  @Override
  public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("xmlObject", xmlObject)
    );
  }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength)
    throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("x", x),
      new StringPair("targetSqlType", targetSqlType),
      new StringPair("scaleOrLength", scaleOrLength)
    );
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("x", x),
      new StringPair("length", length)
    );
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("x", x),
      new StringPair("length", length)
    );
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, long length)
    throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("reader", reader),
      new StringPair("length", length)
    );
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("x", x)
    );
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("x", x)
    );
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("reader", reader)
    );
  }

  @Override
  public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("value", value)
    );
  }

  @Override
  public void setClob(int parameterIndex, Reader reader) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("reader", reader)
    );
  }

  @Override
  public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("inputStream", inputStream)
    );
  }

  @Override
  public void setNClob(int parameterIndex, Reader reader) throws SQLException {
    SheetsDriver.log(10,       new StringPair("parameterIndex", parameterIndex),
      new StringPair("reader", reader)
    );
  }

  @Override
  public ResultSet executeQuery(String sql) throws SQLException {
    return null;
  }

  @Override
  public int executeUpdate(String sql) throws SQLException {
    return 0;
  }

  @Override
  public void close() throws SQLException {

  }

  @Override
  public int getMaxFieldSize() throws SQLException {
    SheetsDriver.log(10);
    return 0;
  }

  @Override
  public void setMaxFieldSize(int max) throws SQLException {
    SheetsDriver.log(10,       new StringPair("max", max)
    );
  }

  @Override
  public int getMaxRows() throws SQLException {
    SheetsDriver.log(10);
    return 0;
  }

  @Override
  public void setMaxRows(int max) throws SQLException {
    SheetsDriver.log(10,       new StringPair("max", max)
    );
  }

  @Override
  public void setEscapeProcessing(boolean enable) throws SQLException {
    SheetsDriver.log(10,       new StringPair("enable", enable)
    );
  }

  @Override
  public int getQueryTimeout() throws SQLException {
    SheetsDriver.log(10);
    return 0;
  }

  @Override
  public void setQueryTimeout(int seconds) throws SQLException {
    SheetsDriver.log(10,       new StringPair("seconds", seconds)
    );
  }

  @Override
  public void cancel() throws SQLException {
    SheetsDriver.log(10);

  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    SheetsDriver.log(10);
    return null;
  }

  @Override
  public void clearWarnings() throws SQLException {
    SheetsDriver.log(10);

  }

  @Override
  public void setCursorName(String name) throws SQLException {
    SheetsDriver.log(10,       new StringPair("name", name)
    );
  }

  @Override
  public boolean execute(String sql) throws SQLException {
    return false;
  }

  @Override
  public ResultSet getResultSet() throws SQLException {
    return null;
  }

  @Override
  public int getUpdateCount() throws SQLException {
    return 0;
  }

  @Override
  public boolean getMoreResults() throws SQLException {
    SheetsDriver.log(10);
    return false;
  }

  @Override
  public void setFetchDirection(int direction) throws SQLException {
    SheetsDriver.log(10,       new StringPair("direction", direction)
    );
  }

  @Override
  public int getFetchDirection() throws SQLException {
    SheetsDriver.log(10);
    return 0;
  }

  @Override
  public void setFetchSize(int rows) throws SQLException {
    SheetsDriver.log(10,       new StringPair("rows", rows)
    );
  }

  @Override
  public int getFetchSize() throws SQLException {
    SheetsDriver.log(10);
    return 0;
  }

  @Override
  public int getResultSetConcurrency() throws SQLException {
    SheetsDriver.log(10);
    return 0;
  }

  @Override
  public int getResultSetType() throws SQLException {
    SheetsDriver.log(10);
    return 0;
  }

  @Override
  public void addBatch(String sql) throws SQLException {
    SheetsDriver.log(10,       new StringPair("sql", sql)
    );
  }

  @Override
  public void clearBatch() throws SQLException {
    SheetsDriver.log(10);

  }

  @Override
  public int[] executeBatch() throws SQLException {
    SheetsDriver.log(10);
    return new int[0];
  }

  @Override
  public Connection getConnection() throws SQLException {
    return null;
  }

  @Override
  public boolean getMoreResults(int current) throws SQLException {
    SheetsDriver.log(10,       new StringPair("current", current)
    );
    return false;
  }

  @Override
  public ResultSet getGeneratedKeys() throws SQLException {
    SheetsDriver.log(10);
    return null;
  }

  @Override
  public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
    SheetsDriver.log(10,       new StringPair("sql", sql),
      new StringPair("autoGeneratedKeys", autoGeneratedKeys)
    );
    return 0;
  }

  @Override
  public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
    SheetsDriver.log(10,       new StringPair("sql", sql),
      new StringPair("columnIndexes", Arrays.toString(columnIndexes))
    );
    return 0;
  }

  @Override
  public int executeUpdate(String sql, String[] columnNames) throws SQLException {
    SheetsDriver.log(10,       new StringPair("sql", sql),
      new StringPair("columnNames", Arrays.toString(columnNames))
    );
    return 0;
  }

  @Override
  public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
    SheetsDriver.log(10,       new StringPair("sql", sql),
      new StringPair("autoGeneratedKeys", autoGeneratedKeys)
    );
    return false;
  }

  @Override
  public boolean execute(String sql, int[] columnIndexes) throws SQLException {
    SheetsDriver.log(10,       new StringPair("sql", sql),
      new StringPair("columnIndexes", Arrays.toString(columnIndexes))
    );
    return false;
  }

  @Override
  public boolean execute(String sql, String[] columnNames) throws SQLException {
    SheetsDriver.log(10,       new StringPair("sql", sql),
      new StringPair("columnNames", Arrays.toString(columnNames))
    );
    return false;
  }

  @Override
  public int getResultSetHoldability() throws SQLException {
    SheetsDriver.log(10);
    return 0;
  }

  @Override
  public boolean isClosed() throws SQLException {
    SheetsDriver.log(10);
    return false;
  }

  @Override
  public void setPoolable(boolean poolable) throws SQLException {
    SheetsDriver.log(10,       new StringPair("poolable", poolable)
    );
  }

  @Override
  public boolean isPoolable() throws SQLException {
    SheetsDriver.log(10);
    return false;
  }

  @Override
  public void closeOnCompletion() throws SQLException {
    SheetsDriver.log(10);

  }

  @Override
  public boolean isCloseOnCompletion() throws SQLException {
    SheetsDriver.log(10);
    return false;
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    return null;
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return false;
  }
}

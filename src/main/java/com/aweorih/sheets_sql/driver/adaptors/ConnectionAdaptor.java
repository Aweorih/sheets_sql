package com.aweorih.sheets_sql.driver.adaptors;

import com.aweorih.sheets_sql.driver.SheetsDriver;
import com.aweorih.sheets_sql.utils.StringPair;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionAdaptor implements Connection {

  private static final Logger LOGGER = LogManager.getLogger(ConnectionAdaptor.class);

  @Override
  public Statement createStatement() throws SQLException {
    SheetsDriver.log(4);
    throw new SQLException("createStatement not supported, use prepareStatement");
  }

  @Override
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    return null;
  }

  @Override
  public CallableStatement prepareCall(String sql) throws SQLException {
    SheetsDriver.log(4, new StringPair("sql", sql));
    throw new SQLException("callable statement not supported");
  }

  @Override
  public String nativeSQL(String sql) throws SQLException {
    return null;
  }

  @Override
  public void setAutoCommit(boolean autoCommit) throws SQLException {
    SheetsDriver.log(4, new StringPair("autoCommit", autoCommit));
//    throw new SQLException("auto commit not supported");
  }

  @Override
  public boolean getAutoCommit() throws SQLException {
    SheetsDriver.log(4);
//    throw new SQLException("auto commit not supported");
    return true;
  }

  @Override
  public void commit() throws SQLException {

    SheetsDriver.log(4);
//    throw new SQLException("auto commit not supported");
  }

  @Override
  public void rollback() throws SQLException {
    SheetsDriver.log(4);
//    throw new SQLException("rollback not supported");
  }

  @Override
  public void close() throws SQLException {

  }

  @Override
  public boolean isClosed() throws SQLException {
    return false;
  }

  @Override
  public DatabaseMetaData getMetaData() throws SQLException {
    SheetsDriver.log(4);
    return null;
  }

  @Override
  public void setReadOnly(boolean readOnly) throws SQLException {
//    SheetsDriver.log(new StringPair("readOnly", readOnly));
//    throw new SQLException("read only not supported");
  }

  @Override
  public boolean isReadOnly() throws SQLException {
    SheetsDriver.log(4);
    return false;
  }

  @Override
  public void setCatalog(String catalog) throws SQLException {
    SheetsDriver.log(4, new StringPair("catalog", catalog));
//    throw new SQLException("catalog not supported");
  }

  @Override
  public String getCatalog() throws SQLException {

    SheetsDriver.log(4);
    return null;
  }

  @Override
  public void setTransactionIsolation(int level) throws SQLException {
    SheetsDriver.log(4, new StringPair("level", level));
//    throw new SQLException("transactionIsolation not supported");
  }

  @Override
  public int getTransactionIsolation() throws SQLException {
    SheetsDriver.log(4);
    return Connection.TRANSACTION_NONE;
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    SheetsDriver.log(4);
    return null;
  }

  @Override
  public void clearWarnings() throws SQLException {
    SheetsDriver.log(4);
//    throw new SQLException("warnings not supported");
  }

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency)
    throws SQLException {
    SheetsDriver.log(
      4,
      new StringPair("resultSetType", resultSetType),
      new StringPair("resultSetConcurrency", resultSetConcurrency)
    );
    return null;
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
    throws SQLException {
    SheetsDriver.log(
      4,
      new StringPair("sql", sql),
      new StringPair("resultSetType", resultSetType),
      new StringPair("resultSetConcurrency", resultSetConcurrency)
    );
    throw new SQLException("function not supported");
  }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
    throws SQLException {
    SheetsDriver.log(
      4,
      new StringPair("sql", sql),
      new StringPair("resultSetType", resultSetType),
      new StringPair("resultSetConcurrency", resultSetConcurrency)
    );
    throw new SQLException("CallableStatement not supported");
  }

  @Override
  public Map<String, Class<?>> getTypeMap() throws SQLException {
    SheetsDriver.log(4);
    throw new SQLException("TypeMap not supported");
  }

  @Override
  public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    SheetsDriver.log(4, new StringPair("map", map));
    throw new SQLException("TypeMap not supported");
  }

  @Override
  public void setHoldability(int holdability) throws SQLException {
    SheetsDriver.log(4, new StringPair("holdability", holdability));
    throw new SQLException("Holdability not supported");
  }

  @Override
  public int getHoldability() throws SQLException {
    SheetsDriver.log(4);
    throw new SQLException("Holdability not supported");
  }

  @Override
  public Savepoint setSavepoint() throws SQLException {
    SheetsDriver.log(4);
    throw new SQLException("Savepoint not supported");
  }

  @Override
  public Savepoint setSavepoint(String name) throws SQLException {
    SheetsDriver.log(4, new StringPair("name", name));
    throw new SQLException("Savepoint not supported");
  }

  @Override
  public void rollback(Savepoint savepoint) throws SQLException {
    SheetsDriver.log(4, new StringPair("savepoint", savepoint));
    throw new SQLException("Savepoint not supported");
  }

  @Override
  public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    SheetsDriver.log(4, new StringPair("savepoint", savepoint));
    throw new SQLException("Savepoint not supported");
  }

  @Override
  public Statement createStatement(
    int resultSetType, int resultSetConcurrency, int resultSetHoldability
  ) throws SQLException {
    SheetsDriver.log(
      4,
      new StringPair("resultSetType", resultSetType),
      new StringPair("resultSetConcurrency", resultSetConcurrency),
      new StringPair("resultSetHoldability", resultSetHoldability)
    );
    throw new SQLException("function not supported");
  }

  @Override
  public PreparedStatement prepareStatement(
    String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability
  ) throws SQLException {
    SheetsDriver.log(
      4,
      new StringPair("sql", sql),
      new StringPair("resultSetType", resultSetType),
      new StringPair("resultSetConcurrency", resultSetConcurrency),
      new StringPair("resultSetHoldability", resultSetHoldability)
    );
    throw new SQLException("function not supported");
  }

  @Override
  public CallableStatement prepareCall(
    String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability
  ) throws SQLException {
    SheetsDriver.log(
      4,
      new StringPair("sql", sql),
      new StringPair("resultSetType", resultSetType),
      new StringPair("resultSetConcurrency", resultSetConcurrency),
      new StringPair("resultSetHoldability", resultSetHoldability)
    );
    throw new SQLException("CallableStatement not supported");
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
    SheetsDriver.log(
      4,
      new StringPair("sql", sql),
      new StringPair("autoGeneratedKeys", autoGeneratedKeys)
    );
    throw new SQLException("prepareStatement with autoGeneratedKeys not supported");
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
    SheetsDriver.log(
      4,
      new StringPair("sql", sql),
      new StringPair("columnIndexes", Arrays.toString(columnIndexes))
    );
    throw new SQLException("prepareStatement with columnIndexes not supported");
  }

  @Override
  public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
    SheetsDriver.log(
      4,
      new StringPair("sql", sql),
      new StringPair("columnNames", Arrays.toString(columnNames))
    );
    throw new SQLException("prepareStatement with columnNames not supported");
  }

  @Override
  public Clob createClob() throws SQLException {
    SheetsDriver.log(4);
    throw new SQLException("CreateClob not supported");
  }

  @Override
  public Blob createBlob() throws SQLException {
    SheetsDriver.log(4);
    throw new SQLException("Blob not supported");
  }

  @Override
  public NClob createNClob() throws SQLException {
    SheetsDriver.log(4);
    throw new SQLException("NClob not supported");
  }

  @Override
  public SQLXML createSQLXML() throws SQLException {
    SheetsDriver.log(4);
    throw new SQLException("SQLXML not supported");
  }

  @Override
  public boolean isValid(int timeout) throws SQLException {
    return false;
  }

  @Override
  public void setClientInfo(String name, String value) throws SQLClientInfoException {

    SheetsDriver.log(
      4,
      new StringPair("name", name),
      new StringPair("value", value)
    );
    throw new SQLClientInfoException("setClientInfo not supported", Collections.emptyMap());
  }

  @Override
  public void setClientInfo(Properties properties) throws SQLClientInfoException {
    SheetsDriver.log(
      4,
      new StringPair("properties", properties)
    );
    throw new SQLClientInfoException("setClientInfo not supported", Collections.emptyMap());
  }

  @Override
  public String getClientInfo(String name) throws SQLException {
    SheetsDriver.log(
      4,
      new StringPair("name", name)
    );
    throw new SQLException("getClientInfo not supported");
  }

  @Override
  public Properties getClientInfo() throws SQLException {
    SheetsDriver.log(4);
    throw new SQLException("getClientInfo not supported");
  }

  @Override
  public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
    SheetsDriver.log(
      4,
      new StringPair("typeName", typeName),
      new StringPair("elements", Arrays.toString(elements))
    );
    throw new SQLException("createArrayOf not supported");
  }

  @Override
  public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
    SheetsDriver.log(
      4,
      new StringPair("typeName", typeName),
      new StringPair("attributes", Arrays.toString(attributes))
    );
    throw new SQLException("createStruct not supported");
  }

  @Override
  public void setSchema(String schema) throws SQLException {

  }

  @Override
  public String getSchema() throws SQLException {
    return null;
  }

  @Override
  public void abort(Executor executor) throws SQLException {
    SheetsDriver.log(
      4,
      new StringPair("executor", executor)
    );
    throw new SQLException("abort not supported");
  }

  @Override
  public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
    SheetsDriver.log(
      4,
      new StringPair("executor", executor),
      new StringPair("milliseconds", milliseconds)
    );
    throw new SQLException("setNetworkTimeout not supported");
  }

  @Override
  public int getNetworkTimeout() throws SQLException {
    SheetsDriver.log(4);
    throw new SQLException("getNetworkTimeout not supported");
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    throw new SQLException("unwrap not supported");
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    throw new SQLException("isWrapperFor not supported");
  }
}

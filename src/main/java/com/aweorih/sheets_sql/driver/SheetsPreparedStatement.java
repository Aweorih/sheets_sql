package com.aweorih.sheets_sql.driver;


import com.aweorih.sheets_sql.core.QueryRunner;
import com.aweorih.sheets_sql.driver.adaptors.PreparedStatementAdaptor;
import com.aweorih.sheets_sql.utils.StringPair;
import com.google.common.base.Verify;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetsPreparedStatement extends PreparedStatementAdaptor {

  private final Connection           connection;
  private final QueryRunner          queryRunner;
  private final String               sql; // can be null
  private final Map<Integer, Object> paramMapping = new HashMap<>();
  private final SheetsMetaCache      sheetsMetaCache;

  private boolean   isClosed      = false;
  // methods should throw only SQLException's
  private ResultSet lastResultSet = null;
  private boolean   hasBeenReset  = false;
  private int       maxResultSize = -1;

  public SheetsPreparedStatement(Connection connection, SheetsMetaCache sheetsMetaCache, QueryRunner queryRunner, String sql) {
    Verify.verifyNotNull(queryRunner);
    Verify.verifyNotNull(connection);
    Verify.verifyNotNull(sheetsMetaCache);
    this.queryRunner = queryRunner;
    this.sql = sql;
    this.connection = connection;
    this.sheetsMetaCache = sheetsMetaCache;
  }

    /* ##################################
  ################# execute methods start #################
  ################################## */

  @Override
  public ResultSet executeQuery() throws SQLException {

    SheetsDriver.log(3);

    List<Map<String, Object>> result = queryRunner.runQuery(getQuery(), paramMapping);

    return ExceptionWrapper.run(() -> {
      this.lastResultSet = new SheetsResultSet(this, result, maxResultSize);
      return this.lastResultSet;
    });
  }

  @Override
  public ResultSet executeQuery(String sql) throws SQLException {

    SheetsDriver.log(
      3,
      new StringPair("sql", sql)
    );

    List<Map<String, Object>> result = queryRunner.runQuery(sql);
    return ExceptionWrapper.run(() -> {
      this.lastResultSet = new SheetsResultSet(this, result, maxResultSize);
      return this.lastResultSet;
    });
  }

  @Override
  public int executeUpdate() throws SQLException {

    SheetsDriver.log(3);

    List<Object> params = ExceptionWrapper.run(this::parseParams);
    queryRunner.runQuery(getQuery(), params);
    return 0;
  }

  @Override
  public boolean execute(String sql) throws SQLException {

    SheetsDriver.log(
      3,
      new StringPair("sql", sql)
    );

    List<Map<String, Object>> result = queryRunner.runQuery(sql);
    if (result.isEmpty()) return false;

    return ExceptionWrapper.run(() -> {
      this.lastResultSet = new SheetsResultSet(this, result, maxResultSize);
      return true;
    });
  }

  @Override
  public boolean execute() throws SQLException {

    SheetsDriver.log(3);
    List<Object>              params = ExceptionWrapper.run(this::parseParams);
    List<Map<String, Object>> result = queryRunner.runQuery(getQuery(), params);

    if (result.isEmpty()) return false;
    return ExceptionWrapper.run(() -> {
      this.lastResultSet = new SheetsResultSet(this, result, maxResultSize);
      return true;
    });
  }

  @Override
  public int executeUpdate(String sql) throws SQLException {

    SheetsDriver.log(
      3,
      new StringPair("sql", sql)
    );

    queryRunner.runQuery(sql);
    return 0;
  }

  /* ##################################
  ################# execute methods end #################
  ################################## */

  @Override
  public void clearParameters() throws SQLException {
    SheetsDriver.log(3);
    paramMapping.clear();
  }

  // @ todo maybe later; after execute methods have been implemented
  @Override
  public void addBatch() throws SQLException {
    SheetsDriver.log(3);

  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException {
    SheetsDriver.log(3);
    if (null == lastResultSet) throw new SQLException("no result set available");
    return lastResultSet.getMetaData();
  }

  // @ todo maybe later because data about given parameters should be known by client side
  @Override
  public ParameterMetaData getParameterMetaData() throws SQLException {
    SheetsDriver.log(3);
    return null;
  }

  @Override
  public void close() throws SQLException {
    SheetsDriver.log(3);
    isClosed = true;
  }

  @Override
  public ResultSet getResultSet() throws SQLException {
    SheetsDriver.log(3);
    return lastResultSet;
  }

  // @ todo after methods for query execution
  @Override
  public int getUpdateCount() throws SQLException {

    int response = null == lastResultSet || null == lastResultSet.getMetaData()
                   ? -1
                   : lastResultSet.getMetaData().getColumnCount();

    if (null != lastResultSet && lastResultSet.isAfterLast()) {
      response = -1;
    }

    SheetsDriver.log(3, new StringPair("response", response));
    return response;

//    if (null == lastResultSet) return -1;
//
//    return lastResultSet.getMetaData().getColumnCount();
  }

  @Override
  public void setMaxRows(int max) throws SQLException {
    SheetsDriver.log(
      3,
      new StringPair("max", max)
    );
    this.maxResultSize = max;
  }

  @Override
  public boolean getMoreResults() throws SQLException {

    if (null != lastResultSet && !hasBeenReset) lastResultSet.beforeFirst();

    // i don't know why...
    hasBeenReset = true;
    boolean response = null != lastResultSet && lastResultSet.next();

    SheetsDriver.log(3, new StringPair("response", response));
    return response;
//    if (null == lastResultSet) return false;
//    return lastResultSet.next();
  }

  @Override
  public Connection getConnection() throws SQLException {
    SheetsDriver.log(3);
    return connection;
  }

  @Override
  public boolean isClosed() throws SQLException {
    SheetsDriver.log(3);
    return isClosed;
  }

  /* ##################################
  ################# set methods start #################
  ################################## */

  @Override
  public void setNull(int parameterIndex, int sqlType) throws SQLException {
    setObject(parameterIndex, null);
  }

  @Override
  public void setBoolean(int parameterIndex, boolean x) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setByte(int parameterIndex, byte x) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setShort(int parameterIndex, short x) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setInt(int parameterIndex, int x) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setLong(int parameterIndex, long x) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setFloat(int parameterIndex, float x) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setDouble(int parameterIndex, double x) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setString(int parameterIndex, String x) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setBytes(int parameterIndex, byte[] x) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setDate(int parameterIndex, Date x) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setTime(int parameterIndex, Time x) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
    setObject(parameterIndex, x);
  }

  @Override
  public void setObject(int parameterIndex, Object x) throws SQLException {
    SheetsDriver.log(
      3,
      new StringPair("parameterIndex", parameterIndex),
      new StringPair("x", x)
    );
    paramMapping.put(parameterIndex, x);
  }

  // @ todo after methods for query execution
  @Override
  public void setRowId(int parameterIndex, RowId x) throws SQLException {

    SheetsDriver.log(3);
  }

  /* ##################################
  ################# set methods end #################
  ################################## */

  private String getQuery() throws SQLException {
    if (sql == null) throw new SQLException("sql query cannot be null");
    return sql;
  }

  private List<Object> parseParams() {

    int          paramSize    = paramMapping.size();
    List<Object> parsedParams = new ArrayList<>();

    for (int i = 1; i <= paramSize; i++) {
      Object valueAtI = paramMapping.get(i);
      Verify.verifyNotNull(valueAtI, "param at positions %s not set", i);
      parsedParams.add(valueAtI);
    }

    return parsedParams;
  }
}

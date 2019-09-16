package com.aweorih.sheets_sql.driver.connection;

import com.aweorih.sheets_sql.core.QueryRunner;
import com.aweorih.sheets_sql.driver.ExceptionWrapper;
import com.aweorih.sheets_sql.driver.SheetsDatabaseMetaData;
import com.aweorih.sheets_sql.driver.SheetsDriver;
import com.aweorih.sheets_sql.driver.SheetsMetaCache;
import com.aweorih.sheets_sql.driver.SheetsPreparedStatement;
import com.aweorih.sheets_sql.driver.adaptors.ConnectionAdaptor;
import com.aweorih.sheets_sql.driver.adaptors.DatabaseMetaDataAdaptor;

import com.aweorih.sheets_sql.utils.StringPair;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class SheetsConnection extends ConnectionAdaptor {

  private              boolean         isClosed        = false;
  private final        QueryRunner     queryRunner;
  private final        UrlParameter    urlParameter;
  private static final SheetsMetaCache sheetsMetaCache = new SheetsMetaCache(true);


  SheetsConnection(UrlParameter urlParameter) {

    this.queryRunner = new QueryRunner(sheetsMetaCache, urlParameter, null, false);
    this.urlParameter = urlParameter;

    try {
      for (String schema : urlParameter.getSchemas()) {

        String id = sheetsMetaCache.getSheetsIdByNameOrDefault(schema, null);

        if (null == id) {
          id = "null";
        }

        String escapedSchema = "`" + schema + "`";
        SheetsDriver.log(6, new StringPair("request", escapedSchema), new StringPair("res", id));

        sheetsMetaCache.addSheetsIdMapping(escapedSchema, id);
        queryRunner.setDatabase(id, false);
      }

      if (urlParameter.getSpreadsheetId().isPresent()) {

        queryRunner.setDatabase(urlParameter.getSpreadsheetId().get(), false);
      }

      SheetsDriver.log(6, new StringPair("parameter", urlParameter.toString()));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public Statement createStatement() throws SQLException {

    SheetsDriver.log(6);
    return ExceptionWrapper.run(
      () -> new SheetsPreparedStatement(this, sheetsMetaCache, queryRunner, null));
  }

  @Override
  public PreparedStatement prepareStatement(String sql) throws SQLException {

    SheetsDriver.log(
      6,
      new StringPair("sql", sql)
    );
    return ExceptionWrapper.run(
      () -> new SheetsPreparedStatement(this, sheetsMetaCache, queryRunner, sql));
  }

  @Override
  public String nativeSQL(String sql) throws SQLException {
    SheetsDriver.log(
      6,
      new StringPair("sql", sql)
    );
    return sql;
  }

  @Override
  public void close() throws SQLException {
    SheetsDriver.log(6);
    isClosed = true;
  }

  @Override
  public boolean isClosed() throws SQLException {
    SheetsDriver.log(6);
    return isClosed;
  }

  @Override
  public DatabaseMetaData getMetaData() throws SQLException {
    SheetsDriver.log(6);
    return new SheetsDatabaseMetaData(sheetsMetaCache, queryRunner, urlParameter.getSchemas());
  }

  // @TODO: 18.08.2019 how?
  @Override
  public boolean isValid(int timeout) throws SQLException {
    SheetsDriver.log(6, new StringPair("timeout", timeout));
    return true;
  }

  @Override
  public void setSchema(String schema) throws SQLException {
    SheetsDriver.log(
      6,
      new StringPair("schema", sheetsMetaCache.getSheetsIdByNameOrDefault(schema, schema))
    );
    try {
      queryRunner.setDatabase(sheetsMetaCache.getSheetsIdByNameOrDefault(schema, schema), true);
    } catch (IOException e) {
      throw new SQLException(e);
    }
  }

  @Override
  public boolean getAutoCommit() throws SQLException {
    SheetsDriver.log(6);
    return true;
  }

  @Override
  public String getSchema() throws SQLException {

    String response = null;
    if (!queryRunner.getDatabases().isEmpty()) {
      response =  queryRunner.getDatabases().get(0);
    }
    SheetsDriver.log(6, new StringPair("response", response));
    return response;
  }

  @Override
  public String getCatalog() throws SQLException {

    SheetsDriver.log(6);
    if (queryRunner.getDatabases().isEmpty()) {
      return null;
    }
    return queryRunner.getDatabases().get(0);
  }
}

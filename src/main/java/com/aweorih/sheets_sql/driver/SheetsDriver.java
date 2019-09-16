package com.aweorih.sheets_sql.driver;

import com.aweorih.sheets_sql.driver.connection.ConnectionBuilder;
import com.aweorih.sheets_sql.utils.FileUtils;
import com.aweorih.sheets_sql.utils.Pair;
import com.aweorih.sheets_sql.utils.StringPair;
import com.google.common.base.VerifyException;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.logging.Logger;

public class SheetsDriver implements Driver {

  private static final String LOG_PATH     = "";

  private static final List<Integer> WANTED_LEVELS = Collections.emptyList();
//  private static final List<Integer> WANTED_LEVELS = Arrays.asList(2, 8, 9);
//  private static final List<Integer> WANTED_LEVELS = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12);

  static {
    try {
      DriverManager.registerDriver(new SheetsDriver());
    } catch (SQLException e) {
      throw new RuntimeException("Could not register driver", e);
    }
  }

  public static void log(int level, Exception e) {
    if (!WANTED_LEVELS.contains(level)) return;
    if (e == null) return;
    try {

      StackTraceElement   stackTraceElement = Thread.currentThread().getStackTrace()[2];
      String              className         = stackTraceElement.getClassName();
      String              methodName        = stackTraceElement.getMethodName();
      StackTraceElement[] elements          = e.getStackTrace();
      StringJoiner        joiner            = new StringJoiner("\n");
      for (StackTraceElement element : elements) {
        joiner.add("  " + element.toString());
      }
      String msg = String.format(
        "%s: caught exception in %s::%s: msg: %s%nstacktrace: %s%n",
        new Timestamp(System.currentTimeMillis()).toString(),
        className,
        methodName,
        e.getMessage(),
        joiner.toString()
      );
      FileUtils.appendStringToFile(LOG_PATH, msg);
    } catch (IOException ignored) {}
  }

  public static void log(int level, StringPair... params) {

    if (!WANTED_LEVELS.contains(level)) return;
    StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];

    String className  = stackTraceElement.getClassName();
    String methodName = stackTraceElement.getMethodName();

    StringJoiner joiner = new StringJoiner(" and ", "with: ", "");

    for (StringPair pair : params) {
      joiner.add(pair.left + ": " + pair.right);
    }

    String with = params.length == 0 ? "" : joiner.toString();
    log(
      String.format("Requested %s::%s %s", className, methodName, with));
  }

  private static void log(String msg) {
    try {
      FileUtils.appendStringToFile(
        LOG_PATH,
        new Timestamp(System.currentTimeMillis()).toString() + ": " + msg + "\n");
    } catch (IOException ignored) {}
  }

  /*
  url schema: 'jdbc:sheets:<ApplicationName>/<SheetId>'
  possible properties:
    tokensDirectoryPath:             defines the path to the google request tokens
    spreadSheetsCredentialsFilePath: defines the path to the credentials for google spreadSheets
   */
  @Override
  public Connection connect(String url, Properties info) throws SQLException {
    log(0, new StringPair("info", info));
    try {
      return new ConnectionBuilder().buildConnection(url, info);
    } catch (VerifyException e) {
      log(0, e);
      throw new SQLException(e);
    }
  }

  @Override
  public boolean acceptsURL(String url) throws SQLException {

    log(0, new StringPair("url", url));
    return ConnectionBuilder.validateUrl(url);
  }

  @Override
  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
    log(0, new StringPair("url", url), new StringPair("info", info));
    return new DriverPropertyInfo[0];
  }

  @Override
  public int getMajorVersion() {
    return 1;
  }

  @Override
  public int getMinorVersion() {
    return 0;
  }

  @Override
  public boolean jdbcCompliant() {
    return false;
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new SQLFeatureNotSupportedException();
  }

  public static String cleanQuotationsSafe(String value) {

    if (null == value || value.length() < 2) return value;

    return value.replace("\"", "")
                .replace("`", "")
                .replace("'", "");
  }
}

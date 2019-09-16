package com.aweorih.sheets_sql.driver.connection;

import com.aweorih.sheets_sql.driver.SheetsDriver;
import com.aweorih.sheets_sql.driver.connection.UrlParameter.UrlParameterBuilder;
import com.aweorih.sheets_sql.utils.StringPair;
import com.aweorih.sheets_sql.utils.Utils;
import com.google.common.base.Verify;
import com.google.common.collect.ImmutableMap;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class ConnectionBuilder {

  private static final Map<String, String> SHEETS_PROPERTIES;
  private static final Map<String, String> DRIVE_PROPERTIES;
  public static final  String              SHEETS_TOKENS_DIRECTORY_PATH_KEY = "sheetsTokensDirectoryPath";
  public static final  String              SHEETS_CREDENTIALS_FILE_PATH_KEY = "sheetsCredentialsFilePath";
  public static final  String              DRIVE_TOKENS_DIRECTORY_PATH_KEY  = "driveTokensDirectoryPath";
  public static final  String              DRIVE_CREDENTIALS_FILE_PATH_KEY  = "driveCredentialsFilePath";

  static {
    Map<String, String> sheetsTmp = new HashMap<>();
    Map<String, String> driveTmp  = new HashMap<>();
    sheetsTmp.put(SHEETS_TOKENS_DIRECTORY_PATH_KEY, "sheetTokens");
    sheetsTmp.put(SHEETS_CREDENTIALS_FILE_PATH_KEY, "config/sheetCredentials.json");

    driveTmp.put(DRIVE_TOKENS_DIRECTORY_PATH_KEY, "driveTokens");
    driveTmp.put(DRIVE_CREDENTIALS_FILE_PATH_KEY, "config/driveCredentials.json");

    SHEETS_PROPERTIES = ImmutableMap.copyOf(sheetsTmp);
    DRIVE_PROPERTIES = ImmutableMap.copyOf(driveTmp);
  }

  public ConnectionBuilder() {}

  // parent catches VerifyException
  public SheetsConnection buildConnection(String url, Properties properties) throws SQLException {

    properties = null == properties ? new Properties() : properties;
    addPropertiesFromUrl(url, properties);
    checkProperties(properties);
    UrlParameter urlParameter = checkRequestedDriver(url, properties);
    try {
      return new SheetsConnection(urlParameter);
    } catch (Exception e) {
      throw new SQLException(e);
    }
  }

  public static boolean validateUrl(String url) {
    try {
      checkRequestedDriver(url);
      return true;
    } catch (Exception ignored) {}
    return false;
  }

  private static UrlParameter checkRequestedDriver(String url) {
    Properties properties = new Properties();
    addPropertiesFromUrl(url, properties);
    checkProperties(properties);
    return checkRequestedDriver(url, properties);
  }

  private static void addPropertiesFromUrl(String url, Properties properties) {

    String[] split = url.split("\\?");

    if (split.length != 2) return;

    String[] explodedProperties = split[1].split("&");

    if (explodedProperties.length == 0) return;

    for (String explodedProperty : explodedProperties) {

      String[] kv = explodedProperty.split("=");
      if (kv.length != 2) continue;
      properties.put(kv[0], kv[1]);
    }
  }

  private static UrlParameter checkRequestedDriver(String url, Properties properties) {

    /*
     'url': 'jdbc:sheets:work'
     */

    Verify.verifyNotNull(url, "url cannot be null");
    Verify.verify(url.startsWith("jdbc:sheets:"),
                  "invalid url, expected url start: 'jdbc:sheets:'");

    propagatePropertiesFromUrl(properties, url);
    SheetsDriver.log(0, new StringPair("properties", Utils.writeJsonWithDefaultSettings(properties)));
    url = url.substring(12);  // remove 'jdbc:sheets:' from start

    String application = url.replaceFirst("^([^/]*).*$", "$1");
    SheetsDriver.log(0, new StringPair("application", application));
    Verify.verify(!application.isEmpty(), "invalid url, no application name provided.");

    String spreadsheetsId = getSpreadSheetsId(url).orElse(null);

    SheetsDriver.log(0, new StringPair("spreadsheetsId", spreadsheetsId));

    checkProperties(properties);

    boolean      isDrive  = isDrive(properties);
    boolean      isSheets = isSheets(properties);
    List<String> schemas  = getSchemas(properties);
    UrlParameterBuilder builder = new UrlParameterBuilder()
      .withApplicationName(application)
      .withSpreadsheetId(spreadsheetsId)
      .withSchemas(schemas);

    if (isDrive) {
      builder
        .withIsDrive(true)
        .withDriveCredentialsFilePath(properties.getProperty(DRIVE_CREDENTIALS_FILE_PATH_KEY))
        .withDriveTokensDirectoryPath(properties.getProperty(DRIVE_TOKENS_DIRECTORY_PATH_KEY));
    }

    if (isSheets) {
      builder
        .withIsSheets(true)
        .withSheetsCredentialsFilePath(properties.getProperty(SHEETS_CREDENTIALS_FILE_PATH_KEY))
        .withSheetsTokensDirectoryPath(
          properties.getProperty(SHEETS_TOKENS_DIRECTORY_PATH_KEY));
    }

    return builder.build();
  }

  private static Optional<String> getSpreadSheetsId(String url) {

    if (!url.contains("?")) return Optional.empty();

    String base = url.substring(0, url.indexOf("?"));

    if (!base.contains("/")) return Optional.empty();

    return Optional.of(base.substring(base.indexOf("/") + 1));
  }

  private static void propagatePropertiesFromUrl(Properties properties, String url) {

    String urlProperties = url.substring(url.indexOf("?") + 1);

    String[] split = urlProperties.split("&");

    for (String urlProperty : split) {

      String[] localSplit = urlProperty.split("=");
      if (localSplit.length != 2) continue;

      properties.put(localSplit[0], localSplit[1]);
    }
  }

  /*
    checks if drive or/and sheets configurations params have been provided
   */
  private static void checkProperties(Properties properties) {

    boolean isDrive  = isDrive(properties);
    boolean isSheets = isSheets(properties);

    Verify.verify(isDrive || isSheets,
                  "either drive or sheets configuration params must be provided");
  }

  private static List<String> getSchemas(Properties properties) {

    String       schemas  = properties.getProperty("schemas");
    String[]     split    = schemas.split(",");
    List<String> response = new ArrayList<>();

    for (String s : split) {
      response.add(s.trim());
    }

    return response;
  }

  private static boolean isDrive(Properties properties) {

    boolean[] is = {true};

    DRIVE_PROPERTIES.forEach((k, v) -> {
      if (properties.containsKey(k)) return;
      is[0] = false;
    });

    return is[0];
  }

  private static boolean isSheets(Properties properties) {

    boolean[] is = {true};

    SHEETS_PROPERTIES.forEach((k, v) -> {
      if (properties.containsKey(k)) return;
      is[0] = false;
    });

    return is[0];
  }
}

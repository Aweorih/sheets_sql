package com.aweorih.sheets_sql.utils;

import com.google.common.base.Verify;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;

public class LoggingUtils {

  private LoggingUtils() {} // never

  public static void bootLogging() {

    try {
      if (!existsLog4j2File()) {
        makeLog4j2File();
      }
    } catch (Exception e) {
      makeLog4j2File();
    }


    String executionPath = System.getProperty("user.dir");
    Configurator.initialize(
      null,
      String.format(
        "%s%sconfig%slog4j2.xml",
        executionPath,
        File.separator,
        File.separator
      )
    );
  }

  public static void logInfo(Class origin, String info) {
    getLogger(origin).info(info);
  }

  public static void logException(Class origin, Exception e) {
    getLogger(origin).error(e.getMessage(), e);
  }

  public static void logException(Class origin, String msg) {
    getLogger(origin).error(msg);
  }

  public static Logger getLogger(Class origin) {
    Verify.verifyNotNull(origin);
    return LogManager.getLogger(origin);
  }

  private static boolean existsLog4j2File() {

    String executionPath = System.getProperty("user.dir");

    return new File(String.format("%s/config/log4j2.xml", executionPath)).exists();
  }

  private static void makeLog4j2File() {

    String filePath = "config/";

    final Pattern pattern     = Pattern.compile(".*core_utils/config.*");
    final String  destination = System.getProperty("user.dir") + "/config";

    Map<String, String> created = FileUtils.unpackJarFile(
      pattern,
      ".*core_utils/config/",
      true,
      destination
    );
  }
}

package com.aweorih.sheets_sql.driver.connection;

import com.google.common.base.Verify;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UrlParameter {

  private final UrlParameterBuilder urlParameterBuilder;

  UrlParameter(UrlParameterBuilder urlParameterBuilder) {

    this.urlParameterBuilder = urlParameterBuilder;
  }

  public Optional<String> getSpreadsheetId() {
    return Optional.ofNullable(urlParameterBuilder.spreadsheetId);
  }

  public List<String> getSchemas() {
    return urlParameterBuilder.schemas;
  }

  public boolean isDrive() {
    return urlParameterBuilder.isDrive;
  }

  public boolean isSheets() {
    return urlParameterBuilder.isSheets;
  }

  public String getApplicationName() {
    return urlParameterBuilder.applicationName;
  }

  public String getSheetsTokensDirectoryPath() {
    Verify.verify(isSheets(), "requested sheets tokens directory path but none provided");
    return urlParameterBuilder.sheetsTokensDirectoryPath;
  }

  public String getSheetsCredentialsFilePath() {
    Verify.verify(isSheets(), "requested sheets credentials file path but none provided");
    return urlParameterBuilder.sheetsCredentialsFilePath;
  }

  public String getDriveTokensDirectoryPath() {
    Verify.verify(isSheets(), "requested drive tokens directory path but none provided");
    return urlParameterBuilder.driveTokensDirectoryPath;
  }

  public String getDriveCredentialsFilePath() {
    Verify.verify(isSheets(), "requested drive credentials file path but none provided");
    return urlParameterBuilder.driveCredentialsFilePath;
  }

  @Override
  public String toString() {
    return urlParameterBuilder.toString();
  }

  public static final class UrlParameterBuilder {

    private List<String> schemas                   = null;
    private String       applicationName           = null;
    private String       sheetsTokensDirectoryPath = null;
    private String       sheetsCredentialsFilePath = null;
    private String       driveTokensDirectoryPath  = null;
    private String       driveCredentialsFilePath  = null;
    private String       spreadsheetId             = null;
    private boolean      isDrive                   = false;
    private boolean      isSheets                  = false;

    public UrlParameter build() {

      boolean isDriveValid = isDrive
                             && driveTokensDirectoryPath != null
                             && driveCredentialsFilePath != null;
      boolean isSheetsValid = isSheets
                              && sheetsTokensDirectoryPath != null
                              && sheetsCredentialsFilePath != null;

      Verify.verify(isDriveValid || isSheetsValid,
                    "either drive or sheets configuration params must be provided");
      Verify.verify(isSheetsValid && applicationName != null,
                    "if sheets configuration params provided, there must be also an application name provided");
//      Verify.verifyNotNull(spreadsheetId, "a spreadsheetId must be provided"); // can be null

      return new UrlParameter(this);
    }

    public UrlParameterBuilder withSchemas(List<String> schemas) {
      this.schemas = schemas == null ? Collections.emptyList() : schemas;

      return this;
    }

    public UrlParameterBuilder withApplicationName(String applicationName) {
      this.applicationName = applicationName;
      return this;
    }

    public UrlParameterBuilder withSheetsTokensDirectoryPath(String sheetsTokensDirectoryPath) {
      this.sheetsTokensDirectoryPath = sheetsTokensDirectoryPath;
      return this;
    }

    public UrlParameterBuilder withSheetsCredentialsFilePath(String sheetsCredentialsFilePath) {
      this.sheetsCredentialsFilePath = sheetsCredentialsFilePath;
      return this;
    }

    public UrlParameterBuilder withDriveTokensDirectoryPath(String driveTokensDirectoryPath) {
      this.driveTokensDirectoryPath = driveTokensDirectoryPath;
      return this;
    }

    public UrlParameterBuilder withDriveCredentialsFilePath(String driveCredentialsFilePath) {
      this.driveCredentialsFilePath = driveCredentialsFilePath;
      return this;
    }

    public UrlParameterBuilder withSpreadsheetId(String spreadsheetId) {
      this.spreadsheetId = spreadsheetId;
      return this;
    }

    public UrlParameterBuilder withIsDrive(boolean isDrive) {
      this.isDrive = isDrive;
      return this;
    }

    public UrlParameterBuilder withIsSheets(boolean isSheets) {
      this.isSheets = isSheets;
      return this;
    }

    @Override
    public String toString() {
      return "UrlParameterBuilder{" + "\n" +
             "applicationName='" + applicationName + '\'' + ",\n" +
             "sheetsTokensDirectoryPath='" + sheetsTokensDirectoryPath + '\'' + ",\n" +
             "sheetsCredentialsFilePath='" + sheetsCredentialsFilePath + '\'' + ",\n" +
             "driveTokensDirectoryPath='" + driveTokensDirectoryPath + '\'' + ",\n" +
             "driveCredentialsFilePath='" + driveCredentialsFilePath + '\'' + ",\n" +
             "spreadsheetId='" + spreadsheetId + '\'' + ",\n" +
             "isDrive=" + isDrive + ",\n" +
             "isSheets=" + isSheets + ",\n" +
             '}';
    }
  }
}

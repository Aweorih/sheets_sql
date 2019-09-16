package com.aweorih.sheets_sql.google_handler.existing;

import com.aweorih.sheets_sql.driver.connection.UrlParameter;
import com.aweorih.sheets_sql.google_handler.DriveHandler;
import com.aweorih.sheets_sql.google_handler.SheetsHandler;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.common.base.Verify;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.aweorih.sheets_sql.utils.FileUtils;
import com.aweorih.sheets_sql.utils.TimeService;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class ServiceCreator {

  private static final Logger LOGGER = LogManager.getLogger(ServiceCreator.class);

  private static final JsonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

  /**
   * Global instance of the scopes required by this quickstart.
   * If modifying these scopes, delete your previously saved tokens/ folder.
   */
  private static final List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS,
                                                           DriveScopes.DRIVE);

  private final UrlParameter urlParameter;
  private final AtomicLong   lastSend = new AtomicLong(System.currentTimeMillis());

  public ServiceCreator(UrlParameter urlParameter) {
    Verify.verifyNotNull(urlParameter);
    this.urlParameter = urlParameter;
  }

  public DriveHandler makeDriveHandler() {
    return new DriveHandler(makeDriveService(), lastSend);
  }

  private Drive makeDriveService() {

    String credentialsFilePath = urlParameter.getDriveCredentialsFilePath();
    String tokensPath          = urlParameter.getDriveTokensDirectoryPath();
    String applicationName     = urlParameter.getApplicationName();

    try {
      // authorization
      // set up the global Drive instance
      final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
      return new Drive.Builder(
        HTTP_TRANSPORT,
        jacksonFactory,
        getCredentials(HTTP_TRANSPORT, credentialsFilePath, tokensPath)
      ).setApplicationName(applicationName).build();
//      service = new Drive.Builder(HTTP_TRANSPORT, jacksonFactory, getCredentials(HTTP_TRANSPORT, driveCredentialsFilePath)).setApplicationName(
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
  }

  public SheetsHandler makeSheetsHandler() {
    return new SheetsHandler(makeSheetsService(), lastSend);
  }

  private Sheets makeSheetsService() {

    String credentialsFilePath = urlParameter.getSheetsCredentialsFilePath();
    String tokensPath          = urlParameter.getSheetsTokensDirectoryPath();
    String applicationName     = urlParameter.getApplicationName();

    try {

      LOGGER.info("creating new google sheets service object");
      // Build a new authorized API client service.
      final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
      return new Sheets
        .Builder(HTTP_TRANSPORT, jacksonFactory,
                 getCredentials(HTTP_TRANSPORT, credentialsFilePath, tokensPath))
        .setApplicationName(applicationName)
        .build();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates an authorized Credential object.
   *
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  private HttpRequestInitializer getCredentials(
    NetHttpTransport HTTP_TRANSPORT,
    String credentialsPath,
    String tokensPath
  )
    throws
    IOException {

    String fileContent = new String(FileUtils.readFileByPathIntoArray(credentialsPath));
    Reader reader      = new StringReader(fileContent);
    // Load client secrets.
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jacksonFactory, reader);

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
      HTTP_TRANSPORT, jacksonFactory, clientSecrets, scopes)
      .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokensPath)))
      .setAccessType("offline")
      .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    Credential credential = new AuthorizationCodeInstalledApp(flow, receiver)
      .authorize("user");

    return request -> {
      credential.initialize(request);
      request.setConnectTimeout((int) TimeService.getMinutesInMillis(1));
      request.setReadTimeout((int) TimeService.getMinutesInMillis(2));
    };
  }
}

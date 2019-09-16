package com.aweorih.sheets_sql.google_handler;

import com.aweorih.sheets_sql.driver.SheetsDriver;
import com.aweorih.sheets_sql.utils.StringPair;
import com.aweorih.sheets_sql.utils.Utils;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.Get;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.common.base.Verify;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class DriveHandler {

  private final Drive      drive;
  private final AtomicLong lastSend;

  public DriveHandler(Drive drive, AtomicLong lastSend) {
    this.drive = drive;
    this.lastSend = lastSend;
  }

  private void checkLastRequestAndSleep() {

    long passed = System.currentTimeMillis() - lastSend.get();
    if (passed > 1000) return;
    Utils.sleep(1000 - passed, getClass());
  }

  private void setLastRequest() {
    lastSend.set(System.currentTimeMillis());
  }

  public Optional<String> getSpreadSheetIdByName(String expected) throws IOException {

    checkLastRequestAndSleep();

    List<Map<String, Object>> databases = listDatabases();
    SheetsDriver.log(12, new StringPair("drive files", Utils.writeJsonWithDefaultSettings(databases)));
    expected = expected.replace(" ", "__________").replace("`", "");

    for (Map<String, Object> map : databases) {

      Object oName = map.get("name");
      Object oId   = map.get("id");
      if (null == oName || null == oId) continue;
      String given = oName.toString().replace(" ", "_");
      if (!given.equals(expected)) continue;

      setLastRequest();
      return Optional.of(oId.toString());
    }

    setLastRequest();
    return Optional.empty();
  }

  public List<Map<String, Object>> listDatabases() throws IOException {

    checkLastRequestAndSleep();

    Files.List list = drive.files().list();

//    list.setFields("*");
    list.setFields("files(id, name, trashed, webViewLink, createdTime)");
    list.setQ("trashed = false");

    FileList fl = list.execute();

    List<File>                files  = fl.getFiles();
    List<Map<String, Object>> result = new ArrayList<>();

    for (File file : files) {
      HashMap<String, Object> map = new HashMap<>(file);
      Object                  o   = map.get("name");
      if (null != o) {
        map.put("name", o.toString().replace(" ", "__________"));
      }
      result.add(map);
    }

    setLastRequest();
    return result;
  }

  public List<Map<String, Object>> listDatabase(
    String databaseNameOrId,
    String showDatabaseFields,
    String query
  ) throws IOException {

    checkLastRequestAndSleep();

    Verify.verifyNotNull(databaseNameOrId);
    Verify.verify(!databaseNameOrId.isEmpty());

    if (query == null || query.isEmpty()) {
      query = String.format("name = '%s'", databaseNameOrId);
    } else {
      query = String.format("%s AND name = '%s'", query, databaseNameOrId);
    }

    Files.List list = drive.files().list().setQ(query);
    Get        get  = drive.files().get(databaseNameOrId);

    if (showDatabaseFields != null && !showDatabaseFields.isEmpty()) {
      list.setFields("files(" + showDatabaseFields + ")");
      get.setFields(showDatabaseFields);
    }

    File     idFile = get.execute();
    FileList fl     = list.execute();

    List<File>                files  = fl.getFiles();
    List<Map<String, Object>> result = new ArrayList<>();
    result.add(new HashMap<>(idFile));

    for (File file : files) {
      result.add(new HashMap<>(file));
    }

    setLastRequest();
    return result;
  }
}

package com.aweorih.sheets_sql.google_handler;

import com.aweorih.sheets_sql.core.update.SheetsUpdateVisitor;
import com.aweorih.sheets_sql.driver.SheetsDriver;
import com.aweorih.sheets_sql.utils.Utils;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.Append;
import com.google.api.services.sheets.v4.model.*;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;

/*
ranges:
    Sheet1!A1:B2 refers to the first two cells in the top two rows of Sheet1.
    Sheet1!A:A   refers to all the cells in the first column of Sheet1.
    Sheet1!1:1   refers to all the cells in the first row of Sheet1.
    Sheet1!1:2   refers to all the cells in the first two rows of Sheet1.
    Sheet1!A5:A  refers to all the cells of the first column of Sheet 1, from row 5 onward.
    A1:B2        refers to the first two cells in the top two rows of the first visible sheet.
    Sheet1       refers to all the cells in Sheet1.
 */
public class SheetsHandler {

  private static final Logger LOGGER = LogManager.getLogger(SheetsHandler.class);

  private       Sheets        service;
  private final UpdateHandler updateHandler;
  private final AtomicLong    lastSend;
//  private       long          lastRequest = System.currentTimeMillis();

  public SheetsHandler(Sheets service, AtomicLong lastSend) {

    this.service = service;
    this.updateHandler = new UpdateHandler(service);
    this.lastSend = lastSend;
  }

//  private BatchUpdateValuesRequest makeBUVRequest(ValueRange... valueRanges) {
//    return new BatchUpdateValuesRequest()
//      .setData(Arrays.asList(valueRanges))
//      .setValueInputOption("USER_ENTERED");
//  }
//
//  public void append(Spreadsheet ss, String range, ValueRange content) throws IOException {
//
//    Append append = service.spreadsheets().values().append(ss.getSpreadsheetId(), range, content);
//    append.setValueInputOption("USER_ENTERED");
//    AppendValuesResponse response = append.execute();
//    String               asd      = "";
//  }

  private void checkLastRequestAndSleep() {

    long passed = System.currentTimeMillis() - lastSend.get();
    if (passed > 1000) return;
    Utils.sleep(1000 - passed, getClass());
  }

  private void setLastRequest() {
    lastSend.set(System.currentTimeMillis());
  }

  public List<Map<String, Object>> read(
    Spreadsheet ss,
    String sheet,
    Map<String, String> columnAliasMapping
  ) throws IOException {

    checkLastRequestAndSleep();

    List<String> ranges = new ArrayList<>();

    columnAliasMapping.keySet().forEach(c -> ranges.add(c + ":" + c));

    List<String>              realRanges = new ArrayList<>();
    List<Map<String, Object>> response   = new ArrayList<>();

    ranges.forEach(r -> realRanges.add(sheet + "!" + r));

    /* send read request to google for given ranges */
    BatchGetValuesResponse result = service.spreadsheets()
                                           .values()
                                           .batchGet(ss.getSpreadsheetId())
                                           .setRanges(realRanges)
                                           .execute();

    /* we can abort fast here if no data is returned */
    if (null == result.getValueRanges()) {
      setLastRequest();
      return Collections.emptyList();
    }

    Map<Integer, List<List<Object>>> mapping = new HashMap<>();
    int[]                            maxSize = {0};

    /*
      get maximum cell count of rows
      put data from google format into our format
     */
    result.getValueRanges().forEach(vr -> {

      int column = IOConverter.extractColumnNumberFromRange(vr.getRange());

      mapping.put(column, vr.getValues());

      if (null == vr.getValues() || vr.getValues().size() < maxSize[0]) return;

      maxSize[0] = vr.getValues().size();
    });

    /* for correct ordering or response maps */
    List<Integer> keys = new ArrayList<>(mapping.keySet());
    Collections.sort(keys);

    /* build response with given data */
    for (int j = 0; j < maxSize[0]; j++) {

      Map<String, Object> map = new LinkedHashMap<>();
      response.add(map);

      for (int i : keys) {

        String             column     = IOConverter.numberToTableColumn(i);
        String             realColumn = columnAliasMapping.getOrDefault(column, column);
        List<List<Object>> values     = mapping.get(i);

        Object toAdd = "";

        if (null != values && j < values.size()) {
          List<Object> value = values.get(j);
          toAdd = value != null && !value.isEmpty() ? value.get(0) : "";
        }

        map.put(realColumn, toAdd);
      }
    }

    setLastRequest();
    return response;
  }

  // range = '${sheet name}' if whole sheet shall be read
  public List<Map<String, Object>> read(String id, String range) throws IOException {
//  public List<Map<String, Object>> read(Spreadsheet ss, String range) throws IOException {

    checkLastRequestAndSleep();

    ValueRange valueRange = service.spreadsheets()
                                   .values()
                                   .get(id, range)
                                   .execute();
    List<List<Object>> response = null == valueRange.getValues()
                                  ? Collections.emptyList()
                                  : valueRange.getValues();

    List<Map<String, Object>> result = new ArrayList<>();
    int maxSize = response
      .stream()
      .mapToInt(List::size)
      .max()
      .orElse(0);

    for (List<Object> list : response) {

      Map<String, Object> map = new LinkedHashMap<>();
      result.add(map);

      for (int i = 0; i < maxSize; i++) {

        Object o = i >= list.size() ? "" : list.get(i);
        map.put(IOConverter.numberToTableColumn(i), o);
      }
//      for (int i = 0; i < list.size(); i++) {
//        Object o = list.get(i);
//
//        map.put(IOConverter.numberToTableColumn(i), o);
//      }
    }

    setLastRequest();
    return result;
  }

  public Spreadsheet generateSpreadsheet(String name) throws IOException {

    Spreadsheet spreadsheet = new Spreadsheet()
      .setProperties(new SpreadsheetProperties().setTitle(name));

    Spreadsheet execute = service.spreadsheets().create(spreadsheet).execute();
    setLastRequest();
    return execute;
  }

  public Spreadsheet getSpreadSheetById(String id) {

    id = SheetsDriver.cleanQuotationsSafe(id);
    checkLastRequestAndSleep();
    Spreadsheet spreadsheet = new Spreadsheet();
    spreadsheet.setSpreadsheetId(id);
    try {
      Spreadsheet execute = service.spreadsheets().get(id).execute();
      setLastRequest();
      return execute;
    } catch (IOException e) {
      throw new RuntimeException("unknown spreadsheet id: " + id);
    }
//    return service.spreadsheets().get(id).execute();
  }

  public BatchUpdateSpreadsheetResponse generateSheet(
    Spreadsheet spreadsheet,
    List<String> sheetNames
  ) throws IOException {

    checkLastRequestAndSleep();

    AddSheetRequest addSheetRequest = new AddSheetRequest();

    Request request = new Request();

    sheetNames.forEach(title -> request.setAddSheet(
      addSheetRequest.setProperties(new SheetProperties().setTitle(title))
    ));

    BatchUpdateSpreadsheetRequest busr = new BatchUpdateSpreadsheetRequest().setRequests(
      Collections.singletonList(
        request));

    BatchUpdateSpreadsheetResponse execute = service.spreadsheets().batchUpdate(
      spreadsheet.getSpreadsheetId(),
      busr
    ).execute();

    setLastRequest();
    return execute;
  }

  public void update(Spreadsheet spreadsheet, String sheetName, SheetsUpdateVisitor updateVisitor)
    throws IOException {

    checkLastRequestAndSleep();

    updateHandler.update(spreadsheet, sheetName, updateVisitor);
    setLastRequest();
  }

  private BatchUpdateValuesRequest makeBUVRequest(ValueRange... valueRanges) {
    return new BatchUpdateValuesRequest()
      .setData(Arrays.asList(valueRanges))
      .setValueInputOption("USER_ENTERED");
  }

  public void push(Spreadsheet spreadsheet, Map<String, List<List<Object>>> paramsWithRange)
//  public void push(Spreadsheet spreadsheet, List<List<Object>> values, String range)
    throws IOException {

    checkLastRequestAndSleep();
//    Map<String, List<List<Object>>> paramsWithRange = new HashMap<>();
    List<ValueRange>                valueRanges     = new ArrayList<>();

    paramsWithRange.forEach((range2, values2) -> {

      ValueRange vr = new ValueRange().setValues(values2);
      vr.setRange(range2);
      valueRanges.add(vr);
    });

    BatchUpdateValuesRequest buvr = new BatchUpdateValuesRequest()
      .setData(valueRanges)
      .setValueInputOption("USER_ENTERED");

    BatchUpdateValuesResponse result = service
      .spreadsheets()
      .values()
      .batchUpdate(
        spreadsheet.getSpreadsheetId(),
        buvr
      )
      .execute();

//    ValueRange valueRange = new ValueRange().setValues(values);
//    valueRange.setRange(range);
//    BatchUpdateValuesRequest content = makeBUVRequest(valueRange);
//
//    BatchUpdateValuesResponse response = service
//      .spreadsheets()
//      .values()
//      .batchUpdate(spreadsheet.getSpreadsheetId(), content)
//      .execute();
//
//    System.out.printf("%d cells appended.", response.getTotalUpdatedCells());

//    AppendValuesResponse result = service
//      .spreadsheets()
//      .values()
//      .append(
//        spreadsheet.getSpreadsheetId(),
//        range,
//        valueRange
//      )
//      .setValueInputOption("USER_ENTERED")
//      .execute();

    setLastRequest();
  }

  public void push2(Spreadsheet spreadsheet, List<List<Object>> values, String range)
    throws IOException {

    checkLastRequestAndSleep();

    ValueRange body = new ValueRange().setValues(values);
    body.setRange(range);
//    BatchUpdateValuesRequest content = makeBUVRequest(body);
//
//    AppendValuesResponse result =
//      service.spreadsheets().values().append(spreadsheet.getSpreadsheetId(), range, body)
//             .setValueInputOption("USER_ENTERED")
//             .execute();
//
//
//    BatchUpdateValuesResponse response = service
//      .spreadsheets()
//      .values()
//      .batchUpdate(spreadsheet.getSpreadsheetId(), content)
//      .execute();
//
//    System.out.printf("%d cells appended.", response.getTotalUpdatedCells());
//    System.out.printf("%d cells appended.", result.getTotalUpdatedCells());

    AppendValuesResponse result = service
      .spreadsheets()
      .values()
      .append(
        spreadsheet.getSpreadsheetId(),
        range,
        body
      )
      .setValueInputOption("USER_ENTERED")
      .execute();
    setLastRequest();
  }
}
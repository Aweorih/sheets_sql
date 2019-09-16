package com.aweorih.sheets_sql.google_handler;

import com.aweorih.sheets_sql.core.update.SheetsUpdateVisitor;
import com.aweorih.sheets_sql.driver.SheetsDriver;
import com.aweorih.sheets_sql.utils.StringPair;
import com.aweorih.sheets_sql.utils.Utils;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.google.common.base.Verify;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateHandler {

  private Sheets service;

  UpdateHandler(Sheets service) {

    this.service = service;
  }

  /*
  performs an update with given data
    here happens only some bootstrapping
    if both, row indicator (e.g. 'row > ?') and direct cell references (e.g. 'A1') are in the statement, the row indicators will be ignored
   */
  public void update(Spreadsheet spreadsheet, String sheetName, SheetsUpdateVisitor updateVisitor)
    throws IOException {

    BatchUpdateSpreadsheetRequest req = new BatchUpdateSpreadsheetRequest();

    sheetName = SheetsDriver.cleanQuotationsSafe(sheetName);

    SheetsDriver.log(
      7,
      new StringPair("ss", Utils.writeJsonWithDefaultSettings(spreadsheet)),
      new StringPair("uv", updateVisitor.toString()),
      new StringPair("sn", sheetName)
    );

    List<String>             columns          = updateVisitor.getColumns();
    List<Object>             values           = new ArrayList<>(updateVisitor.getValues());
    List<RowData>            rowData          = valuesToRowData(values);
    Map<String, Integer>     directReferences = getAndRemoveDirectReferences(columns);
    List<UpdateCellsRequest> updateCellsRequests;

    int rowStart = getStart(updateVisitor);
    int rowEnd   = getEnd(updateVisitor, rowStart);

    Verify.verify(
      !directReferences.isEmpty() || (rowStart != -1 && rowEnd != -1),
      "either row ranges or direct cell references must be provided for update statement");


    Optional<Integer> opSheetsId = IOConverter.getIdFromSpreadSheet(spreadsheet, sheetName);
    Verify.verify(opSheetsId.isPresent(), "unknown sheets name: " + sheetName);

    if (directReferences.isEmpty()) {

      updateCellsRequests = toRequest(opSheetsId.get(), rowStart, rowEnd, columns, rowData);
    } else {
      updateCellsRequests = toRequest(opSheetsId.get(), directReferences, rowData);
    }

    List<Request> requests = new ArrayList<>();

    for (UpdateCellsRequest ucr : updateCellsRequests) {
      Request r = new Request();
      requests.add(r);
      r.setUpdateCells(ucr);
    }

    req.setRequests(requests);

    service.spreadsheets().batchUpdate(spreadsheet.getSpreadsheetId(), req).execute();
  }

  /*
  creates final single cell update requests in google format
    finds the row start and end numbers (1 and 2 if nothing provided like 'row > ?')
    creates cell update requests based on given column names and row data
    groups data rather by column than row (all rows per column in one request)
   */
  private List<UpdateCellsRequest> toRequest(
    int sheetsId,
    Map<String, Integer> references, // as in the order of the original query
    List<RowData> rowData
  ) {

    List<UpdateCellsRequest> response = new ArrayList<>();
    int                      i        = 0;

    for (Map.Entry<String, Integer> entry : references.entrySet()) {

      if (i >= rowData.size()) continue;

      UpdateCellsRequest ucr = new UpdateCellsRequest();
      response.add(ucr);
      RowData   rd       = rowData.get(i++);
      String    column   = entry.getKey();
      int       start    = IOConverter.columnToNumber(column);
      int       end      = start + 1; // only one column per row
      GridRange gr       = new GridRange();
      int       rowStart = entry.getValue() - 1;
      int       rowEnd   = rowStart + 1;
      gr.setSheetId(sheetsId);
      gr.setStartColumnIndex(start);
      gr.setEndColumnIndex(end);
      gr.setStartRowIndex(rowStart);
      gr.setEndRowIndex(rowEnd);

      List<RowData> rowDataItems = new ArrayList<>();
      /* every cell shall have the same value, note: should be only 1 cell per row */
      for (int j = rowStart; j < rowEnd; j++) {
        rowDataItems.add(rd);
      }

      ucr.setRows(rowDataItems);
      ucr.setRange(gr);
      ucr.setFields("*");
    }

    return response;
  }

  /*
  creates final single cell update requests in google format
    finds the row start and end numbers (1 and 2 if nothing provided like 'row > ?')
    creates cell update requests based on given column names and row data
    groups data rather by column than row (all rows per column in one request)
   */
  private List<UpdateCellsRequest> toRequest(
    int sheetsId,
    int rowStart,
    int rowEnd,
    List<String> columns,
    List<RowData> rowData
  ) {

    Verify.verify(rowStart >= 0, "row start cannot be lower than 0");
    Verify.verify(rowEnd > 0, "row end cannot be lower than 1");
    Verify.verify(rowStart < rowEnd, "row end cannot be lower than row start");

    List<UpdateCellsRequest> response = new ArrayList<>();

    /* 'i' is here the shared index of 'rowData' and 'columns' */
    for (int i = 0; i < columns.size(); i++) {

      if (i >= rowData.size()) continue;

      UpdateCellsRequest ucr = new UpdateCellsRequest();
      response.add(ucr);
      RowData rd     = rowData.get(i);
      String  column = columns.get(i);

      int       start = IOConverter.columnToNumber(column);
      int       end   = start + 1; // only 1 column per row
      GridRange gr    = new GridRange();

      gr.setSheetId(sheetsId);
      gr.setStartColumnIndex(start);
      gr.setEndColumnIndex(end);
      gr.setStartRowIndex(rowStart);
      gr.setEndRowIndex(rowEnd);

      List<RowData> rowDataItems = new ArrayList<>();
      /* every cell shall have the same value, note: should be only 1 cell per row */
      for (int j = rowStart; j < rowEnd; j++) {
        rowDataItems.add(rd);
      }

      ucr.setRows(rowDataItems);
      ucr.setRange(gr);
      /* apply to all fields. here maybe later where clauses if support is then provided by google for query */
      ucr.setFields("*");
    }

    return response;
  }

  /*
  checks if 1 or more letters are followed by 1 or more digits
    returns mapping of column name to row in provided order
   */
  private Map<String, Integer> getAndRemoveDirectReferences(List<String> columns) {

    Map<String, Integer> response = new LinkedHashMap<>();
    Pattern              pattern  = Pattern.compile("([a-zA-Z]+)([0-9]+)");

    ListIterator<String> iter = columns.listIterator();

    while (iter.hasNext()) {

      String  possibleReference = iter.next();
      Matcher matcher           = pattern.matcher(possibleReference);

      if (!matcher.matches()) continue;
      String  column = matcher.group(1);
      Integer number = Integer.parseInt(matcher.group(2));

      response.put(column, number);

      iter.remove();
    }

    return response;
  }

  /*
  determines the first requested row number
    checks if 'row > ?', 'row >= ?' or 'row = ?' is in the statement
    if not present, returns -1
    if so, returns the requested start position
    if multiple provided only one wil be used by following order:
      = wins over >
      > wins over >=
   */
  private int getStart(SheetsUpdateVisitor updateVisitor) {

    Optional<Integer> opRowGreater       = updateVisitor.getRowGreaterThan();
    Optional<Integer> opRowGreaterEquals = updateVisitor.getRowGreaterThanEquals();
    Optional<Integer> opRowEqualsTo      = updateVisitor.getRowEqualsTo();

    if (opRowEqualsTo.isPresent()) return opRowEqualsTo.get();

    if (opRowGreater.isPresent()) {
      return opRowGreater.get() + 1;
    }

    return opRowGreaterEquals.orElse(-1);
  }

  /*
  determines the last request row number
    checks if 'row < ?', 'row <= ?' or 'row = ?' is in the statement
    if not present, returns rowStart + 1
    if so, returns the requested start position
    if multiple provided only one wil be used by following order:
      = wins over <
      < wins over <=
   */
  private int getEnd(SheetsUpdateVisitor updateVisitor, int rowStart) {

    Optional<Integer> opRowMinor       = updateVisitor.getRowMinorThan();
    Optional<Integer> opRowMinorEquals = updateVisitor.getRowMinorThanEquals();
    Optional<Integer> opRowEqualsTo    = updateVisitor.getRowEqualsTo();

    if (opRowEqualsTo.isPresent()) return opRowEqualsTo.get() + 1;

    if (opRowMinor.isPresent()) {
      return opRowMinor.get() - 1;
    }

    return opRowMinorEquals.orElse(rowStart + 1);
  }

  /*
  basically: transforms query parameter each into a RowData object
    theoretically is it possible to combine multiple values into one RowData
    but then we must respect the column boundaries, e.g:
      Update a,b,f,g ...
    -> requires null values in between for missing columns c,d,e
    also possible to make some changes upstream
   */
  private List<RowData> valuesToRowData(List<Object> values) {

    List<RowData> response = new ArrayList<>();

    for (Object value : values) {

      RowData rd = new RowData();
      response.add(rd);

      CellData      cd = new CellData();
      ExtendedValue ev = new ExtendedValue();
      ev.setStringValue(value.toString());
      cd.setUserEnteredValue(ev);
      rd.setValues(Collections.singletonList(cd));
    }

    return response;
  }
}

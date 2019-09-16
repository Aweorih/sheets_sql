package com.aweorih.sheets_sql.google_handler;

import com.aweorih.sheets_sql.utils.Pair;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class IOConverter {

  private IOConverter() {
    throw new AssertionError();
  }// never

  public static int extractColumnNumberFromRange(String range) {
    return IOConverter.columnToNumber(IOConverter.extractColumnNameFromRange(range));
  }

  public static List<Pair<String, Integer>> getColumnSizeMappingFromSelect(
    List<Map<String, Object>> select
  ) {

    List<Pair<String, Integer>> list = new ArrayList<>();

    for (Map<String, Object> result : select) {

      List<String> keys   = new ArrayList<>(result.keySet());
      List<Object> values = new ArrayList<>(result.values());

      for (int i = 0; i < keys.size(); i++) {

        String key   = keys.get(i);
        String value = i >= values.size() ? "" : values.get(i).toString();

        if (i >= list.size()) {
          list.add(new Pair<>(key, value.length()));
          continue;
        }

        Pair<String, Integer> comparedElement = list.remove(i);

        if (comparedElement.left.equals(key)) {

          int newSize = Math.max(comparedElement.right, value.length());
          list.add(i, new Pair<>(key, newSize));
        } else {
          list.add(i, comparedElement);
          list.add(new Pair<>(key, value.length()));
        }
      }
    }

    return list;
  }

  public static String extractColumnNameFromRange(String range) {
    // does not work with x:y ranges
    // extracts column name
    return range.replaceAll("^[^!]*!([A-Za-z]*).*$", "$1");
  }

  public static Optional<Integer> getIdFromSpreadSheet(Spreadsheet sh, String wanted) {

    List<Sheet> sheets = sh.getSheets();

    if (null == sheets || sheets.isEmpty()) return Optional.empty();

    for (Sheet s : sheets) {

      SheetProperties props = s.getProperties();

      if (null == props || !props.getTitle().equals(wanted)) continue;

      return Optional.of(props.getSheetId());
    }

    return Optional.empty();
  }

  public static int columnToNumber(String column) {

    char[] chars  = column.toCharArray();
    int    result = 0;

    for (int i = 0; i < chars.length; i++) {

      int c   = Character.toUpperCase(chars[i]);
      int v   = c - 64;
      int pow = chars.length - 1 - i;
      int k   = (int) Math.pow(25, pow);
      result += k * v;
    }

    return result - 1;
  }

  public static String numberToTableColumn(int i) {

    StringBuilder       builder   = new StringBuilder();
    List<List<Boolean>> overflows = new ArrayList<>();
    List<Boolean>       current   = null;
    while (i > 25) {
      if (current == null || current.size() > 25) {
        current = new ArrayList<>();
        overflows.add(current);
      }
      current.add(true);
      i -= 26;
    }

    for (List<Boolean> list : overflows) {
      if (list.isEmpty()) continue;
      builder.append((char) (65 + list.size() - 1));
    }

    builder.append((char) (65 + i));

    return builder.toString();
  }
}

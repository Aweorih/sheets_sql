package com.aweorih.sheets_sql.core.shared.expression_visitors;

import com.google.common.base.Verify;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.schema.Column;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SheetsColumnVisitor extends ExpressionVisitorAdapter {

  private final Map<String, String> columnAliasMapping = new LinkedHashMap<>();
  private       List<String>        columns            = new ArrayList<>();

  @Override
  public void visit(Column column) {
    super.visit(column);
    columns.add(column.getColumnName());
    columnAliasMapping.put(column.getColumnName().toUpperCase(), column.getColumnName());
  }

  public void setAliasForLastColumn(Alias alias) {
    Verify.verify(!columnAliasMapping.isEmpty());
    String key = new ArrayList<>(columnAliasMapping.keySet()).get(columnAliasMapping.size() - 1);
    columnAliasMapping.replace(key, alias.getName());
  }

  public List<String> getColumns() {
    return columns;
  }

  public Map<String, String> getColumnAliasMapping() {
    return columnAliasMapping;
  }
}

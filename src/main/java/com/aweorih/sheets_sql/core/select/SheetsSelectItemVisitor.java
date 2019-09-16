package com.aweorih.sheets_sql.core.select;

import com.aweorih.sheets_sql.core.shared.expression_visitors.SheetsColumnVisitor;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter;

import java.util.List;
import java.util.Map;

public class SheetsSelectItemVisitor extends SelectItemVisitorAdapter {

  private final SheetsColumnVisitor expressionVisitor = new SheetsColumnVisitor();

  @Override
  public void visit(SelectExpressionItem item) {
    super.visit(item);

    if (null == item.getExpression()) return;

    item.getExpression().accept(expressionVisitor);

    if (null == item.getAlias()) return;

    expressionVisitor.setAliasForLastColumn(item.getAlias());
  }

  public Map<String, String> getColumnAliasMapping() {
    return expressionVisitor.getColumnAliasMapping();
  }

  public List<String> getColumns() {
    return expressionVisitor.getColumns();
  }
}

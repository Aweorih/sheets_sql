package com.aweorih.sheets_sql.core.select;

import com.aweorih.sheets_sql.core.shared.expression_visitors.UnknownVisitor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SheetsSelectVisitor extends SelectVisitorAdapter {

  private final SheetsSelectItemVisitor selectItemVisitor = new SheetsSelectItemVisitor();
  private final SheetsFromItemVisitor   fromItemVisitor   = new SheetsFromItemVisitor();

  @Override
  public void visit(PlainSelect plainSelect) {
    super.visit(plainSelect);

    plainSelect.getSelectItems().forEach(item -> item.accept(selectItemVisitor));
    plainSelect.getFromItem().accept(fromItemVisitor);

    Expression where = plainSelect.getWhere();
    if (null != where) where.accept(new UnknownVisitor());
  }

  public Optional<String> getDatabase() {
    return fromItemVisitor.getDatabase();
  }

  public void setDatabase(String database) {
    fromItemVisitor.setDatabase(database);
  }

  public Optional<String> getTable() {
    return fromItemVisitor.getTable();
  }

  public List<String> getColumns() {
    return selectItemVisitor.getColumns();
  }

  public Map<String, String> getColumnAliasMapping() {
    return selectItemVisitor.getColumnAliasMapping();
  }
}

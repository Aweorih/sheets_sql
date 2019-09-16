package com.aweorih.sheets_sql.core.update;

import com.aweorih.sheets_sql.core.shared.ParamProvider;
import com.aweorih.sheets_sql.core.shared.SheetsTableVisitor;
import com.aweorih.sheets_sql.core.shared.expression_visitors.SheetsColumnVisitor;
import com.aweorih.sheets_sql.core.shared.expression_visitors.SheetsValueVisitor;
import com.aweorih.sheets_sql.core.shared.expression_visitors.SheetsWhereVisitor;
import net.sf.jsqlparser.statement.update.Update;

import java.util.List;
import java.util.Optional;

public class SheetsUpdateVisitor {

  private final SheetsValueVisitor  valueVisitor;
  private final SheetsColumnVisitor columnVisitor = new SheetsColumnVisitor();
  private final SheetsTableVisitor  tableVisitor  = new SheetsTableVisitor();
  private final SheetsWhereVisitor  whereVisitor;

  public SheetsUpdateVisitor() {
    this.valueVisitor = new SheetsValueVisitor();
    this.whereVisitor  = new SheetsWhereVisitor();
  }

  public SheetsUpdateVisitor(ParamProvider paramProvider) {
    this.valueVisitor = new SheetsValueVisitor(paramProvider);
    this.whereVisitor  = new SheetsWhereVisitor(paramProvider);
  }

  public void visit(Update update) {

    update.getTables().forEach(tableVisitor::visit);

    update.getColumns().forEach(columnVisitor::visit);

    update.getExpressions().forEach(e -> e.accept(valueVisitor));

    if (null != update.getWhere()) {

      update.getWhere().accept(whereVisitor);
    }
  }

  public List<String> getColumns() {
    return columnVisitor.getColumns();
  }

  public List<String> getValues() {
    return valueVisitor.getValues();
  }

  public Optional<String> getDatabase() {
    return tableVisitor.getDatabase();
  }

  public Optional<String> getTable() {
    return tableVisitor.getTable();
  }

  public Optional<Integer> getRowGreaterThan() {
    return whereVisitor.getRowGreaterThan();
  }

  public Optional<Integer> getRowMinorThan() {
    return whereVisitor.getRowMinorThan();
  }

  public Optional<Integer> getRowGreaterThanEquals() {
    return whereVisitor.getRowGreaterThanEquals();
  }

  public Optional<Integer> getRowMinorThanEquals() {
    return whereVisitor.getRowMinorThanEquals();
  }

  public Optional<Integer> getRowEqualsTo() {
    return whereVisitor.getRowEqualsTo();
  }
}

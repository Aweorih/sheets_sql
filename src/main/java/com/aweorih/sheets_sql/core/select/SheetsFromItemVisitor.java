package com.aweorih.sheets_sql.core.select;

import com.aweorih.sheets_sql.core.shared.SheetsTableVisitor;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItemVisitorAdapter;

import java.util.Optional;

public class SheetsFromItemVisitor extends FromItemVisitorAdapter {

  private final SheetsTableVisitor tableVisitor = new SheetsTableVisitor();

  @Override
  public void visit(Table table) {
    super.visit(table);
    tableVisitor.visit(table);
  }

  public Optional<String> getDatabase() {
    return tableVisitor.getDatabase();
  }

  public void setDatabase(String database) {
    tableVisitor.setDatabase(database);
  }

  public Optional<String> getTable() {
    return tableVisitor.getTable();
  }
}

package com.aweorih.sheets_sql.core.insert;

import com.aweorih.sheets_sql.core.shared.ParamProvider;
import com.aweorih.sheets_sql.core.shared.expression_visitors.SheetsColumnVisitor;
import com.aweorih.sheets_sql.core.shared.SheetsTableVisitor;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.insert.Insert;

import java.util.List;
import java.util.Optional;

public class SheetsInsertVisitor {

  private final SheetsColumnVisitor    expressionVisitor = new SheetsColumnVisitor();
  private final SheetsTableVisitor     tableVisitor      = new SheetsTableVisitor();
  private final SheetsItemsListVisitor itemsListVisitor;

  private ParamProvider paramProvider = null;

  public SheetsInsertVisitor() {
    itemsListVisitor = new SheetsItemsListVisitor();
  }

  public SheetsInsertVisitor(ParamProvider paramProvider) {
    itemsListVisitor = new SheetsItemsListVisitor(paramProvider);
  }

  public void accept(Insert insert) {

    tableVisitor.visit(insert.getTable());
    // columns
    List<Column> columns = insert.getColumns();
    if (null != columns) columns.forEach(column -> column.accept(expressionVisitor));
    // values
    insert.getItemsList().accept(itemsListVisitor);
  }

  public Optional<String> getDatabase() {
    return tableVisitor.getDatabase();
  }

  public Optional<String> getTable() {
    return tableVisitor.getTable();
  }

  public List<String> getColumns() {
    return expressionVisitor.getColumns();
  }

  /*
  returns groups of values
    'List<String>' represents a group of values
   */
  public List<List<String>> getValues() {
    return itemsListVisitor.getValues();
  }
}

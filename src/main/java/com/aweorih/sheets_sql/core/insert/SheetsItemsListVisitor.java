package com.aweorih.sheets_sql.core.insert;

import com.aweorih.sheets_sql.core.shared.ParamProvider;
import com.aweorih.sheets_sql.core.shared.expression_visitors.SheetsColumnVisitor;
import com.aweorih.sheets_sql.core.shared.expression_visitors.SheetsValueVisitor;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitorAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SheetsItemsListVisitor extends ItemsListVisitorAdapter {

  private final List<SheetsValueVisitor>          valueVisitors      = new ArrayList<>();
  private final Supplier<SheetsValueVisitor> expressionVisitorSupplier;
  private final SheetsColumnVisitor columnVisitor = new SheetsColumnVisitor();

  public SheetsItemsListVisitor() {
    expressionVisitorSupplier = SheetsValueVisitor::new;
  }

  public SheetsItemsListVisitor(ParamProvider paramProvider) {
    expressionVisitorSupplier = () -> new SheetsValueVisitor(paramProvider);
  }

  @Override
  public void visit(ExpressionList expressionList) {
    super.visit(expressionList);
    SheetsValueVisitor expressionVisitor = expressionVisitorSupplier.get();
    valueVisitors.add(expressionVisitor);

    expressionList
      .getExpressions()
      .forEach(expression -> {
        expression.accept(expressionVisitor);
        expression.accept(columnVisitor);
      });
  }

  public List<String> getColumns() {

    Set<String> columns = new HashSet<>(columnVisitor.getColumns());
    return new ArrayList<>(columns);
  }

  public List<List<String>> getValues() {
    return valueVisitors.stream()
                             .map(SheetsValueVisitor::getValues)
                             .collect(Collectors.toList());
  }
}

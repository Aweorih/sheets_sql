package com.aweorih.sheets_sql.core.shared.expression_visitors;

import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;

public class UnknownVisitor extends ExpressionVisitorAdapter {
  public UnknownVisitor() {

    System.out.println("hello");
  }

  @Override
  public void visit(EqualsTo expr) {
    super.visit(expr);

    System.out.println("hello ");
    System.out.println(expr.getLeftExpression().toString());
    System.out.println(expr.getRightExpression().toString());
  }
}

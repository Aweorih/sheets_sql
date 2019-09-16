package com.aweorih.sheets_sql.core.shared.expression_visitors;

import com.aweorih.sheets_sql.core.shared.ParamProvider;
import com.google.common.base.Verify;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.relational.*;

import java.util.Optional;

public class SheetsWhereVisitor extends ExpressionVisitorAdapter {

  private       Integer       rowGreaterThan       = null;
  private       Integer       rowGreaterThanEquals = null;
  private       Integer       rowMinorThan         = null;
  private       Integer       rowMinorThanEquals   = null;
  private       Integer       rowEqualsTo          = null;
  private final ParamProvider paramProvider;

  public SheetsWhereVisitor() {
    this.paramProvider = null;
  }

  public SheetsWhereVisitor(ParamProvider paramProvider) {
    this.paramProvider = paramProvider;
  }

  @Override
  public void visit(EqualsTo expr) {
    super.visit(expr);

    String left = expr.getLeftExpression().toString();
    if (!left.equalsIgnoreCase("row")) return;

    Verify.verify(null == rowGreaterThanEquals, "only one allowed of: 'row =' and 'row >='");
    Verify.verify(null == rowGreaterThan, "only one allowed of: 'row =' and 'row >='");
    Verify.verify(null == rowMinorThanEquals, "only one allowed of: 'row =' and 'row >='");
    Verify.verify(null == rowMinorThan, "only one allowed of: 'row =' and 'row >='");

    String right = expr.getRightExpression().toString();

    if (null != paramProvider && right.equals("?")) {
      rowEqualsTo = Integer.parseInt(paramProvider.getParam());
    } else {
      rowEqualsTo = Integer.parseInt(expr.getRightExpression().toString());
    }
  }

  @Override
  public void visit(GreaterThan expr) {
    super.visit(expr);

    String left = expr.getLeftExpression().toString();
    if (!left.equalsIgnoreCase("row")) return;

    Verify.verify(null == rowEqualsTo, "only one allowed of: 'row =' and 'row >'");
    Verify.verify(null == rowGreaterThanEquals, "only one allowed of: 'row >' and 'row >='");

    String right = expr.getRightExpression().toString();

    if (null != paramProvider && right.equals("?")) {
      rowGreaterThan = Integer.parseInt(paramProvider.getParam());
    } else {
      rowGreaterThan = Integer.parseInt(expr.getRightExpression().toString());
    }
  }

  @Override
  public void visit(GreaterThanEquals expr) {
    super.visit(expr);

    String left = expr.getLeftExpression().toString();
    if (!left.equalsIgnoreCase("row")) return;
    Verify.verify(null == rowGreaterThan, "only one allowed of: 'row >' and 'row >='");
    Verify.verify(null == rowEqualsTo, "only one allowed of: 'row =' and 'row >='");

    String right = expr.getRightExpression().toString();

    if (null != paramProvider && right.equals("?")) {
      rowGreaterThanEquals = Integer.parseInt(paramProvider.getParam());
    } else {
      rowGreaterThanEquals = Integer.parseInt(expr.getRightExpression().toString());
    }
  }

  @Override
  public void visit(MinorThan expr) {
    super.visit(expr);

    String left = expr.getLeftExpression().toString();
    if (!left.equalsIgnoreCase("row")) return;
    Verify.verify(null == rowEqualsTo, "only one allowed of: 'row =' and 'row <'");
    Verify.verify(null == rowMinorThanEquals, "only one allowed of: 'row <' and 'row <='");

    String right = expr.getRightExpression().toString();

    if (null != paramProvider && right.equals("?")) {
      rowMinorThan = Integer.parseInt(paramProvider.getParam());
    } else {
      rowMinorThan = Integer.parseInt(expr.getRightExpression().toString());
    }
  }

  @Override
  public void visit(MinorThanEquals expr) {
    super.visit(expr);

    String left = expr.getLeftExpression().toString();
    if (!left.equalsIgnoreCase("row")) return;
    Verify.verify(null == rowEqualsTo, "only one allowed of: 'row =' and 'row <='");
    Verify.verify(null == rowMinorThan, "only one allowed of: 'row <' and 'row <='");

    String right = expr.getRightExpression().toString();

    if (null != paramProvider && right.equals("?")) {
      rowMinorThanEquals = Integer.parseInt(paramProvider.getParam());
    } else {
      rowMinorThanEquals = Integer.parseInt(expr.getRightExpression().toString());
    }
  }

  public Optional<Integer> getRowGreaterThan() {
    return Optional.ofNullable(rowGreaterThan);
  }

  public Optional<Integer> getRowGreaterThanEquals() {
    return Optional.ofNullable(rowGreaterThanEquals);
  }

  public Optional<Integer> getRowMinorThanEquals() {
    return Optional.ofNullable(rowMinorThanEquals);
  }

  public Optional<Integer> getRowMinorThan() {
    return Optional.ofNullable(rowMinorThan);
  }

  public Optional<Integer> getRowEqualsTo() {
    return Optional.ofNullable(rowEqualsTo);
  }
}

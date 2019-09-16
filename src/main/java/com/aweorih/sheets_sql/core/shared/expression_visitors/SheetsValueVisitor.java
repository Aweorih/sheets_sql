package com.aweorih.sheets_sql.core.shared.expression_visitors;

import com.aweorih.sheets_sql.core.shared.ParamProvider;
import com.google.common.base.Verify;
import net.sf.jsqlparser.expression.*;

import java.util.ArrayList;
import java.util.List;

public class SheetsValueVisitor extends ExpressionVisitorAdapter {

  private List<String>  values        = new ArrayList<>();
  private ParamProvider paramProvider = null;

  public SheetsValueVisitor() {}

  public SheetsValueVisitor(ParamProvider paramProvider) {
    this.paramProvider = paramProvider;
  }

  @Override
  public void visit(JdbcParameter parameter) {
    super.visit(parameter);
    Verify.verifyNotNull(
      paramProvider,
      "jdbc parameters not available when no params provided"
    );
    visit(paramProvider.getParam());
  }

  @Override
  public void visit(NullValue value) {
    super.visit(value);
    visit(value.toString());
  }

  @Override
  public void visit(DoubleValue value) {
    super.visit(value);
    visit(value.toString());
  }

  @Override
  public void visit(LongValue value) {
    super.visit(value);
    visit(value.toString());
  }

  @Override
  public void visit(DateValue value) {
    super.visit(value);
    visit(value.toString());
  }

  @Override
  public void visit(TimeValue value) {
    super.visit(value);
    visit(value.toString());
  }

  @Override
  public void visit(TimestampValue value) {
    super.visit(value);
    visit(value.toString());
  }

  @Override
  public void visit(StringValue value) {
    super.visit(value);
    visit(value.getValue());
  }

  @Override
  public void visit(HexValue hexValue) {
    super.visit(hexValue);
    visit(hexValue.toString());
  }

  private void visit(String value) {
    values.add(value);
  }

  public List<String> getValues() {
    return values;
  }
}

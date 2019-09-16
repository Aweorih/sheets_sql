package com.aweorih.sheets_sql.core;

import com.aweorih.sheets_sql.core.insert.SheetsInsertVisitor;
import com.aweorih.sheets_sql.core.select.SheetsSelectVisitor;
import com.aweorih.sheets_sql.core.shared.ParamProvider;
import com.aweorih.sheets_sql.core.update.SheetsUpdateVisitor;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.UseStatement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import java.util.Optional;

public class SheetsStatementVisitor extends StatementVisitorAdapter {


  private final SheetsSelectVisitor selectVisitor    = new SheetsSelectVisitor();
  private final SheetsInsertVisitor insertVisitor;
  private final SheetsUpdateVisitor updateVisitor   ;
  private       String              useStatementName = null;
  private       boolean             isUpdateVisited  = false;
  private       boolean             isInsertVisited  = false;
  private       boolean             isUseVisited     = false;
  private       boolean             isSelectVisited  = false;

  public SheetsStatementVisitor() {
    this.insertVisitor = new SheetsInsertVisitor();
    this.updateVisitor    = new SheetsUpdateVisitor();
  }

  public SheetsStatementVisitor(ParamProvider paramProvider) {
    this.updateVisitor    = new SheetsUpdateVisitor(paramProvider);
    this.insertVisitor = new SheetsInsertVisitor(paramProvider);
  }

  @Override
  public void visit(Update update) {
    super.visit(update);
    isUpdateVisited = true;
    updateVisitor.visit(update);
  }

  @Override
  public void visit(Select select) {
    super.visit(select);
    isSelectVisited = true;

    select.getSelectBody().accept(selectVisitor);
  }

  @Override
  public void visit(UseStatement use) {
    super.visit(use);
    isUseVisited = true;
    useStatementName = use.getName();
  }

  @Override
  public void visit(Insert insert) {
    super.visit(insert);

    isInsertVisited = true;
    insertVisitor.accept(insert);
  }

  @Override
  public void visit(CreateTable createTable) {
    super.visit(createTable);
    String asd = "";
  }

  public SheetsSelectVisitor getSelectVisitor() {
    return selectVisitor;
  }

  public SheetsInsertVisitor getInsertVisitor() {
    return insertVisitor;
  }

  public Optional<String> getUseStatementName() {
    return Optional.ofNullable(useStatementName);
  }

  public SheetsUpdateVisitor getUpdateVisitor() {
    return updateVisitor;
  }

  public boolean isInsertVisited() {
    return isInsertVisited;
  }

  public boolean isUseVisited() {
    return isUseVisited;
  }

  public boolean isSelectVisited() {
    return isSelectVisited;
  }

  public boolean isUpdateVisited() {
    return isUpdateVisited;
  }
}

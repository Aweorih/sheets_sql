package com.aweorih.sheets_sql;

import com.aweorih.sheets_sql.core.SheetsStatementVisitor;
import com.aweorih.sheets_sql.core.shared.ParamProvider;
import com.aweorih.sheets_sql.core.update.SheetsUpdateVisitor;
import com.google.common.base.Verify;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class Test {

  public static void main(String[] args) throws JSQLParserException {

    List<Object> params = new ArrayList<>();
    String       query  = "UPDATE foobar SET a = 1 WHERE b = 'asd'";


    Statement              stmt          = CCJSqlParserUtil.parse(query);
    List<String>           sqlParams     = reduceParams(params);
    ParamProvider          paramProvider = new ParamProvider(sqlParams);
    SheetsStatementVisitor visitor       = new SheetsStatementVisitor(paramProvider);

    stmt.accept(visitor);

    Verify.verify(visitor.isUpdateVisited(), " update not visited");

    SheetsUpdateVisitor update = visitor.getUpdateVisitor();

    update.getColumns().forEach(System.out::println);
    update.getValues().forEach(System.out::println);
  }

  private static List<String> reduceParams(Object... params) {

    List<String> response = new ArrayList<>();

    for (Object o : params) {
      if (List.class.isAssignableFrom(o.getClass())) {
        reduceList(response, (List) o);
      } else {
        response.add(o.toString());
      }
    }

    return response;
  }

  private static void reduceList(List<String> toAdd, List l) {

    for (Object o : l) {
      if (List.class.isAssignableFrom(o.getClass())) {
        reduceList(toAdd, (List) o);
      } else {
        toAdd.add(o.toString());
      }
    }
  }
}

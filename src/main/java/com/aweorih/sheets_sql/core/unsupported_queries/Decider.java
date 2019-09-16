package com.aweorih.sheets_sql.core.unsupported_queries;

import com.aweorih.sheets_sql.core.QueryRunner;
import com.aweorih.sheets_sql.core.SheetsCreateTableVisitor;
import com.aweorih.sheets_sql.core.consumer.TokenConsumer;
import com.google.common.base.Verify;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Decider {

  private final TokenConsumer translator;

  public Decider(TokenConsumer translator) {
    Verify.verifyNotNull(translator);
    this.translator = translator;
  }

  public Optional<List<Map<String, Object>>> check(String query, Object... params) {

    String s = query.toLowerCase();

    if (s.contains("use database")) {

      String database = parseUseDatabase(query, QueryRunner.reduceParams(params));

      return Optional.of(
        Collections.singletonList(
          Collections.singletonMap("database", database)
        )
      );
    }
    else if (s.matches("^create table ([a-zA-Z]*|\\?)$")) {

      String table = parseCreateTable(query, QueryRunner.reduceParams(params));

      return Optional.of(
        Collections.singletonList(
          Collections.singletonMap("database", table)
        )
      );
    }

    return Optional.empty();
  }

  // create table ?
  private String parseCreateTable(String query, List<String> params) {

    String[] split = query.split(" ");
    Verify.verify(
      split.length == 3, "must be of format'USE DATABASE {Value}'"
    );

    String table = split[2];

    if (table.equals("?")) {
      Verify.verify(params.size() == 1, "expected one param for \"USE DATABASE\" statment");
      table = params.get(0);
    }

    translator.consumeCreateToken(new SheetsCreateTableVisitor(table));

    return table;
  }

  // use database ?
  private String parseUseDatabase(String query, List<String> params) {

    String[] split = query.split(" ");
    Verify.verify(
      split.length == 3, "must be of format'USE DATABASE {Value}'"
    );

    String database = split[2];

    if (database.equals("?")) {
      Verify.verify(params.size() == 1, "expected one param for \"USE DATABASE\" statment");
      database = params.get(0);
    }

    translator.consumeUseToken(database);

    return database;
  }
}

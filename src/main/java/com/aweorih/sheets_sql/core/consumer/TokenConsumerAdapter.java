package com.aweorih.sheets_sql.core.consumer;

import com.aweorih.sheets_sql.core.SheetsCreateTableVisitor;
import com.aweorih.sheets_sql.core.insert.SheetsInsertVisitor;
import com.aweorih.sheets_sql.core.select.SheetsSelectVisitor;
import com.aweorih.sheets_sql.core.update.SheetsUpdateVisitor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TokenConsumerAdapter extends TokenConsumer {

  @Override
  default List<Map<String, Object>> getTables(String schemaPattern, String schemaName) {

    return null;
  }

  @Override
  default Optional<List<Map<String, Object>>> consumeUseToken(String useToken) {
    return Optional.empty();
  }

  @Override
  default Optional<List<Map<String, Object>>> consumeCreateToken(
    SheetsCreateTableVisitor tableVisitor
  ) {
    return Optional.empty();
  }

  @Override
  default Optional<List<Map<String, Object>>> consumeInsertToken(
    SheetsInsertVisitor insertVisitor
  ) {
    return Optional.empty();
  }

  @Override
  default Optional<List<Map<String, Object>>> consumeSelectToken(
    SheetsSelectVisitor selectVisitor
  ) {
    return Optional.empty();
  }

  @Override
  default Optional<List<Map<String, Object>>> consumeUpdateToken(
    SheetsUpdateVisitor updateVisitor
  ) {
    return Optional.empty();
  }

  @Override
  default void setDatabase(String database) {

  }

  @Override
  default List<String> getDatabases() {
    return Collections.emptyList();
  }
}

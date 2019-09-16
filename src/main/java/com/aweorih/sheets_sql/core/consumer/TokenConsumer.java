package com.aweorih.sheets_sql.core.consumer;

import com.aweorih.sheets_sql.core.SheetsCreateTableVisitor;
import com.aweorih.sheets_sql.core.insert.SheetsInsertVisitor;
import com.aweorih.sheets_sql.core.select.SheetsSelectVisitor;
import com.aweorih.sheets_sql.core.update.SheetsUpdateVisitor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TokenConsumer {

  Optional<List<Map<String, Object>>> consumeUseToken(String useToken);

  Optional<List<Map<String, Object>>> consumeCreateToken(SheetsCreateTableVisitor tableVisitor);

  Optional<List<Map<String, Object>>> consumeInsertToken(SheetsInsertVisitor insertVisitor);

  Optional<List<Map<String, Object>>> consumeSelectToken(SheetsSelectVisitor selectVisitor);

  Optional<List<Map<String, Object>>> consumeUpdateToken(SheetsUpdateVisitor updateVisitor);

  List<Map<String, Object>> getTables(String schemaPattern, String schemaName);

  void setDatabase(String database);

  List<String> getDatabases();
}

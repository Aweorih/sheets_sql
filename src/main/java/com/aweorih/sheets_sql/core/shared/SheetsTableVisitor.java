package com.aweorih.sheets_sql.core.shared;

import net.sf.jsqlparser.schema.Table;

import java.util.Optional;

public class SheetsTableVisitor {

  private String database = null;
  private String table    = null;

  public void visit(Table table) {
    setDatabaseFromFQDN(table.getFullyQualifiedName());
    setTableFromFQDN(table.getFullyQualifiedName());
  }

  public Optional<String> getDatabase() {
    return Optional.ofNullable(database);
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  public Optional<String> getTable() {
    return Optional.ofNullable(table);
  }

  // "`Dongle Stack Format`"."`AES Message`"
  private void setDatabaseFromFQDN(String fqdn) {

    String  trimmed       = fqdn.trim();
    boolean startsEscaped = trimmed.startsWith("`") || trimmed.startsWith("\"");
    // `escaped db`.`escaped table`
    boolean hasDbAndTable = trimmed.contains(".");

    if (startsEscaped && !hasDbAndTable) return;

    String[] split = fqdn.split("\\.");
    if (split.length != 2) return;
    database = split[0].replace("\"", "");
  }

  // google table names are wrapped with '`' if they contain a whitespace
  // we remove the wrappings here
  private void setTableFromFQDN(String fqdn) {

    String  trimmed       = fqdn.trim();
    boolean startsEscaped = trimmed.startsWith("`") || trimmed.startsWith("\"");
    // `escaped db`.`escaped table`
    boolean hasDbAndTable = trimmed.contains(".");

    if (trimmed.startsWith("`") && !hasDbAndTable) {
      this.table = removeAndEscape(trimmed, "`");
      return;
    }

    if (trimmed.startsWith("\"") && !hasDbAndTable) {
      // thank you for different implementations in datagrip 2019.2.3 and intellij 2019.2.1
      // db name: "foo bar"
      // datagrip: SELECT t.* FROM "foo bar" t
      // intellij: SELECT t.* FROM foo bar t
      this.table = removeAndEscape(trimmed, "\"");
      return;
    }

    String[] split = fqdn.split("\\.");
    if (split.length == 2) table = removeAndEscape(split[1].replace("\"", ""), "`");
    else if (split.length == 1) table = removeAndEscape(split[0].replace("\"", ""), "`");
  }

  private String removeAndEscape(String source, String toRemove) {
    return removeAndEscape(source, toRemove, "");
  }

  private String removeAndEscape(String source, String toRemove, String replace) {
    return String.format("'%s'", source.replace(toRemove, replace));
  }
}

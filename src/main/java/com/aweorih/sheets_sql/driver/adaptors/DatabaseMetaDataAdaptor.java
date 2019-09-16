package com.aweorih.sheets_sql.driver.adaptors;

import com.aweorih.sheets_sql.driver.SheetsDriver;
import com.aweorih.sheets_sql.utils.StringPair;
import com.google.api.services.sheets.v4.Sheets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseMetaDataAdaptor implements DatabaseMetaData {

  private static final Logger LOGGER = LogManager.getLogger(DatabaseMetaDataAdaptor.class);

  public DatabaseMetaDataAdaptor() {

  }

  @Override
  public boolean allProceduresAreCallable() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean allTablesAreSelectable() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public String getURL() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public String getUserName() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public boolean isReadOnly() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean nullsAreSortedHigh() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean nullsAreSortedLow() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean nullsAreSortedAtStart() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean nullsAreSortedAtEnd() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public String getDatabaseProductName() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public String getDatabaseProductVersion() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public String getDriverName() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public String getDriverVersion() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public int getDriverMajorVersion() {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getDriverMinorVersion() {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public boolean usesLocalFiles() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean usesLocalFilePerTable() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsMixedCaseIdentifiers() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean storesUpperCaseIdentifiers() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean storesLowerCaseIdentifiers() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean storesMixedCaseIdentifiers() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public String getIdentifierQuoteString() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public String getSQLKeywords() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public String getNumericFunctions() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public String getStringFunctions() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public String getSystemFunctions() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public String getTimeDateFunctions() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public String getSearchStringEscape() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public String getExtraNameCharacters() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public boolean supportsAlterTableWithAddColumn() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsAlterTableWithDropColumn() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsColumnAliasing() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean nullPlusNonNullIsNull() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsConvert() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsConvert(int fromType, int toType) throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsTableCorrelationNames() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsDifferentTableCorrelationNames() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsExpressionsInOrderBy() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsOrderByUnrelated() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsGroupBy() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsGroupByUnrelated() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsGroupByBeyondSelect() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsLikeEscapeClause() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsMultipleResultSets() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsMultipleTransactions() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsNonNullableColumns() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsMinimumSQLGrammar() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsCoreSQLGrammar() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsExtendedSQLGrammar() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsANSI92EntryLevelSQL() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsANSI92IntermediateSQL() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsANSI92FullSQL() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsIntegrityEnhancementFacility() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsOuterJoins() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsFullOuterJoins() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsLimitedOuterJoins() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public String getSchemaTerm() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public String getProcedureTerm() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public String getCatalogTerm() throws SQLException {
    SheetsDriver.log(2);
    return "database";
  }

  @Override
  public boolean isCatalogAtStart() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public String getCatalogSeparator() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public boolean supportsSchemasInDataManipulation() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsSchemasInProcedureCalls() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsSchemasInTableDefinitions() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsSchemasInIndexDefinitions() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsCatalogsInDataManipulation() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsCatalogsInProcedureCalls() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsCatalogsInTableDefinitions() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsPositionedDelete() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsPositionedUpdate() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsSelectForUpdate() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsStoredProcedures() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsSubqueriesInComparisons() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsSubqueriesInExists() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsSubqueriesInIns() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsSubqueriesInQuantifieds() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsCorrelatedSubqueries() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsUnion() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsUnionAll() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public int getMaxBinaryLiteralLength() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxCharLiteralLength() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxColumnNameLength() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxColumnsInGroupBy() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxColumnsInIndex() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxColumnsInOrderBy() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxColumnsInSelect() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxColumnsInTable() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxConnections() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxCursorNameLength() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxIndexLength() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxSchemaNameLength() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxProcedureNameLength() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxCatalogNameLength() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxRowSize() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public int getMaxStatementLength() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxStatements() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxTableNameLength() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxTablesInSelect() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getMaxUserNameLength() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getDefaultTransactionIsolation() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public boolean supportsTransactions() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public ResultSet getProcedures(
    String catalog, String schemaPattern, String procedureNamePattern
  ) throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getProcedureColumns(
    String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern
  ) throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getTables(
    String catalog, String schemaPattern, String tableNamePattern, String[] types
  ) throws SQLException {
    SheetsDriver.log(
      2,
      new StringPair("catalog", catalog),
      new StringPair("schemaPattern", schemaPattern),
      new StringPair("tableNamePattern", tableNamePattern),
      new StringPair("types", Arrays.toString(types))
    );
    return null;
  }

  @Override
  public ResultSet getSchemas() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getCatalogs() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getTableTypes() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getColumns(
    String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern
  ) throws SQLException {
    SheetsDriver.log(
      2,
      new StringPair("catalog", catalog),
      new StringPair("schemaPattern", schemaPattern),
      new StringPair("tableNamePattern", tableNamePattern),
      new StringPair("columnNamePattern", columnNamePattern)
    );
    return null;
  }

  @Override
  public ResultSet getColumnPrivileges(
    String catalog, String schema, String table, String columnNamePattern
  ) throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getTablePrivileges(
    String catalog, String schemaPattern, String tableNamePattern
  ) throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getBestRowIdentifier(
    String catalog, String schema, String table, int scope, boolean nullable
  ) throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getVersionColumns(String catalog, String schema, String table)
    throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getImportedKeys(String catalog, String schema, String table)
    throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getExportedKeys(String catalog, String schema, String table)
    throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getCrossReference(
    String parentCatalog, String parentSchema, String parentTable, String foreignCatalog,
    String foreignSchema, String foreignTable
  ) throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getTypeInfo() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getIndexInfo(
    String catalog, String schema, String table, boolean unique, boolean approximate
  ) throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public boolean supportsResultSetType(int type) throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean ownUpdatesAreVisible(int type) throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean ownDeletesAreVisible(int type) throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean ownInsertsAreVisible(int type) throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean othersUpdatesAreVisible(int type) throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean othersDeletesAreVisible(int type) throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean othersInsertsAreVisible(int type) throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean updatesAreDetected(int type) throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean deletesAreDetected(int type) throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean insertsAreDetected(int type) throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsBatchUpdates() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public ResultSet getUDTs(
    String catalog, String schemaPattern, String typeNamePattern, int[] types
  ) throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public Connection getConnection() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public boolean supportsSavepoints() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsNamedParameters() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsMultipleOpenResults() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsGetGeneratedKeys() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public ResultSet getSuperTypes(
    String catalog, String schemaPattern, String typeNamePattern
  ) throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getSuperTables(
    String catalog, String schemaPattern, String tableNamePattern
  ) throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getAttributes(
    String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern
  ) throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public boolean supportsResultSetHoldability(int holdability) throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public int getResultSetHoldability() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getDatabaseMajorVersion() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getDatabaseMinorVersion() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getJDBCMajorVersion() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getJDBCMinorVersion() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public int getSQLStateType() throws SQLException {
    SheetsDriver.log(2);
    return 0;
  }

  @Override
  public boolean locatorsUpdateCopy() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean supportsStatementPooling() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public RowIdLifetime getRowIdLifetime() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
    SheetsDriver.log(2, new StringPair("catalog", catalog), new StringPair("schemaPattern", schemaPattern));
    return null;
  }

  @Override
  public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public ResultSet getClientInfoProperties() throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getFunctions(
    String catalog, String schemaPattern, String functionNamePattern
  ) throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getFunctionColumns(
    String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern
  ) throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public ResultSet getPseudoColumns(
    String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern
  ) throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public boolean generatedKeyAlwaysReturned() throws SQLException {
    SheetsDriver.log(2);
    return false;
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    SheetsDriver.log(2);
    return null;
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    SheetsDriver.log(2);
    return false;
  }
}

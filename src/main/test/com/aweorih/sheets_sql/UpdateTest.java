package com.aweorih.sheets_sql;

import com.aweorih.sheets_sql.core.QueryRunner;
import com.aweorih.sheets_sql.core.consumer.TokenConsumerAdapter;
import com.aweorih.sheets_sql.core.update.SheetsUpdateVisitor;
import com.aweorih.sheets_sql.driver.SheetsMetaCache;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

import static com.aweorih.sheets_sql.TestingData.UPDATE_MULTIPLE_COLUMNS_VALUES;
import static com.aweorih.sheets_sql.TestingData.UPDATE_WITH_ROWS_VALUES;

public class UpdateTest implements TokenConsumerAdapter {

  private final QueryRunner queryRunner   = new QueryRunner(new SheetsMetaCache(true), TestingData.getUrlParameter(), this,
                                                            true);
  private final String      expectedTable = "'FOOBAR'";

  private List<String> expectedColumns          = Collections.emptyList();
  private List<String> expectedValues           = Collections.emptyList();
  /* from here: -1 == Optional.empty() */
  private int          expectedRowEqualsTo      = -1;
  private int          expectedRowGreater       = -1;
  private int          expectedRowGreaterEquals = -1;
  private int          expectedRowMinor         = -1;
  private int          expectedRowMinorEquals   = -1;
  private boolean      hasTriggered             = false;

  private void setDefaults() {
    expectedColumns = Collections.emptyList();
    expectedRowEqualsTo = -1;
    expectedRowGreater = -1;
    expectedRowGreaterEquals = -1;
    expectedRowMinor = -1;
    expectedRowMinorEquals = -1;
    hasTriggered = false;
  }

  @Test
  public void testMultipleColumns() throws SQLException {

    setDefaults();

    expectedColumns = Arrays.asList("c1", "b1");
    expectedValues = Arrays.asList("43", "hallo");

    run(UPDATE_MULTIPLE_COLUMNS_VALUES.query, UPDATE_MULTIPLE_COLUMNS_VALUES.params);
  }

  @Test
  public void testWithRows() throws SQLException {

    setDefaults();

    expectedColumns = Collections.singletonList("c1");
    expectedValues = Collections.singletonList("42");
    expectedRowGreaterEquals = 0;
    expectedRowMinor = 3;

    run(UPDATE_WITH_ROWS_VALUES.query, UPDATE_WITH_ROWS_VALUES.params);
  }

  /*
  tests provided query with given parameters, once inlined (replacing '?') and once as list
   */
  private void run(String query, Object... params) throws SQLException {

    String       inlinedQuery = query;
    List<String> paramsList   = new ArrayList<>();

    for (Object o : params) {
      String value = o.toString();
      paramsList.add(value);
      if (o.getClass() == String.class) {
        value = "'" + value + "'";
      }
      inlinedQuery = inlinedQuery.replaceFirst("\\?", value);
    }

    queryRunner.runQuery(query, paramsList);
    Assert.assertTrue(hasTriggered);
    hasTriggered = false;
    queryRunner.runQuery(inlinedQuery);
    Assert.assertTrue(hasTriggered);
  }

  @Override
  public Optional<List<Map<String, Object>>> consumeUpdateToken(SheetsUpdateVisitor updateVisitor) {

    hasTriggered = true;

    List<String> columns = updateVisitor.getColumns();
    List<Object> values  = new ArrayList<>(updateVisitor.getValues());

    Optional<String> opTable = updateVisitor.getTable();

    Optional<Integer> opRowEqualsTo = updateVisitor.getRowEqualsTo();

    Optional<Integer> opRowGreater       = updateVisitor.getRowGreaterThan();
    Optional<Integer> opRowGreaterEquals = updateVisitor.getRowGreaterThanEquals();

    Optional<Integer> opRowMinor       = updateVisitor.getRowMinorThan();
    Optional<Integer> opRowMinorEquals = updateVisitor.getRowMinorThanEquals();

    Assert.assertEquals(expectedColumns.size(), columns.size());
    Assert.assertEquals(expectedValues.size(), values.size());

    for (int i = 0; i < expectedColumns.size(); i++) {
      String expected = expectedColumns.get(i);
      String actual   = columns.get(i);
      Assert.assertEquals(expected, actual);
    }

    for (int i = 0; i < expectedValues.size(); i++) {
      String expected = expectedValues.get(i);
      String actual   = values.get(i).toString();
      Assert.assertEquals(expected, actual);
    }

    Assert.assertTrue(opTable.isPresent());
    Assert.assertEquals(expectedTable, opTable.get());

    if (expectedRowEqualsTo != -1) {
      Assert.assertTrue(opRowEqualsTo.isPresent());
      Assert.assertEquals((Integer) expectedRowEqualsTo, opRowEqualsTo.get());
    }

    if (expectedRowGreater != -1) {
      Assert.assertTrue(opRowGreater.isPresent());
      Assert.assertEquals((Integer) expectedRowGreater, opRowGreater.get());
    }

    if (expectedRowGreaterEquals != -1) {
      Assert.assertTrue(opRowGreaterEquals.isPresent());
      Assert.assertEquals((Integer) expectedRowGreaterEquals, opRowGreaterEquals.get());
    }

    if (expectedRowMinor != -1) {
      Assert.assertTrue(opRowMinor.isPresent());
      Assert.assertEquals((Integer) expectedRowMinor, opRowMinor.get());
    }

    if (expectedRowMinorEquals != -1) {
      Assert.assertTrue(opRowMinorEquals.isPresent());
      Assert.assertEquals((Integer) expectedRowMinorEquals, opRowMinorEquals.get());
    }

    return Optional.empty();
  }
}

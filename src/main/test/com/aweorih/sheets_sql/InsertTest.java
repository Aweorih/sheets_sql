package com.aweorih.sheets_sql;

import com.aweorih.sheets_sql.core.QueryRunner;
import com.aweorih.sheets_sql.core.consumer.TokenConsumerAdapter;
import com.aweorih.sheets_sql.core.insert.SheetsInsertVisitor;
import com.aweorih.sheets_sql.driver.SheetsMetaCache;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

import static com.aweorih.sheets_sql.TestingData.MULTIPLE_INSERT_VALUES;
import static com.aweorih.sheets_sql.TestingData.SINGLE_INSERT_VALUES;

public class InsertTest implements TokenConsumerAdapter {

  private List<Integer> values = Collections.emptyList();

  @Test
  public void testSingle() throws SQLException {

    values = Arrays.asList(1, 2, 3);

    QueryRunner queryRunner = new QueryRunner(new SheetsMetaCache(true), TestingData.getUrlParameter(), this, true);

    queryRunner.runQuery(SINGLE_INSERT_VALUES.query, SINGLE_INSERT_VALUES.params);
  }

  @Test
  public void testMultiple() throws SQLException {

    values = Arrays.asList(1, 2, 3, 4, 5, 6);

    QueryRunner queryRunner = new QueryRunner(new SheetsMetaCache(true), TestingData.getUrlParameter(), this, true);

    queryRunner.runQuery(MULTIPLE_INSERT_VALUES.query, MULTIPLE_INSERT_VALUES.params);
  }

  @Override
  public Optional<List<Map<String, Object>>> consumeInsertToken(SheetsInsertVisitor insertVisitor) {

    Optional<String>   opTable      = insertVisitor.getTable();
    List<List<String>> insertValues = insertVisitor.getValues();
    List<String>       columns      = insertVisitor.getColumns();

    int groups = values.size() / 3;

    Assert.assertEquals(groups, insertValues.size());

    for (int i = 0; i < groups; i++) {

      List<String> data = insertValues.get(i);
      Assert.assertEquals(Integer.toString((i * 3) + 1), data.get(0));
    }

    Assert.assertEquals("a", columns.get(0));
    Assert.assertEquals("b", columns.get(1));
    Assert.assertEquals("c", columns.get(2));

    Assert.assertTrue(opTable.isPresent());
    Assert.assertEquals("'foobar'", opTable.get());

    return Optional.empty();
  }
}

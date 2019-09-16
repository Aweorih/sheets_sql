package com.aweorih.sheets_sql;

import com.aweorih.sheets_sql.core.QueryRunner;
import com.aweorih.sheets_sql.core.SheetsCreateTableVisitor;
import com.aweorih.sheets_sql.core.consumer.TokenConsumerAdapter;
import com.aweorih.sheets_sql.driver.SheetsMetaCache;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.aweorih.sheets_sql.TestingData.CREATE_TABLE_VALUES;

public class CreateTest implements TokenConsumerAdapter {

  private       int    successful    = 0;
  private final String expectedTable = "foobar";

  @Test
  public void test() throws SQLException {

    QueryRunner queryRunner = new QueryRunner(new SheetsMetaCache(true), TestingData.getUrlParameter(), this, true);

    queryRunner.runQuery(CREATE_TABLE_VALUES.query, CREATE_TABLE_VALUES.params);

    Assert.assertEquals(1, successful);

    queryRunner.runQuery("CREATE TABLE " + expectedTable);

    Assert.assertEquals(2, successful);
  }

  @Override
  public Optional<List<Map<String, Object>>> consumeCreateToken(SheetsCreateTableVisitor tableVisitor) {

    String table = tableVisitor.getTable();

    if (table.equals(expectedTable)) successful++;

    return Optional.empty();
  }
}

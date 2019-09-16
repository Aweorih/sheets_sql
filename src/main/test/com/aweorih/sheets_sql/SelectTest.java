package com.aweorih.sheets_sql;

import com.aweorih.sheets_sql.core.QueryRunner;
import com.aweorih.sheets_sql.core.consumer.TokenConsumerAdapter;
import com.aweorih.sheets_sql.core.select.SheetsSelectVisitor;
import com.aweorih.sheets_sql.driver.SheetsMetaCache;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.aweorih.sheets_sql.TestingData.SELECT_VALUES;

public class SelectTest implements TokenConsumerAdapter {

  @Test
  public void test() throws SQLException {

    QueryRunner queryRunner = new QueryRunner(new SheetsMetaCache(true), TestingData.getUrlParameter(), this, true);

    queryRunner.runQuery(SELECT_VALUES.query);
  }

  @Override
  public Optional<List<Map<String, Object>>> consumeSelectToken(SheetsSelectVisitor selectVisitor) {

    Optional<String> opTable = selectVisitor.getTable();

    List<String> columns = selectVisitor.getColumns();

    Assert.assertTrue(opTable.isPresent());
    Assert.assertEquals("'foobar'", opTable.get());
    Assert.assertEquals("a1", columns.get(0));
    Assert.assertEquals("b", columns.get(1));
    Assert.assertEquals("c", columns.get(2));

    return Optional.empty();
  }
}

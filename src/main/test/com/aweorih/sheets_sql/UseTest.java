package com.aweorih.sheets_sql;

import com.aweorih.sheets_sql.core.QueryRunner;
import com.aweorih.sheets_sql.core.consumer.TokenConsumerAdapter;
import com.aweorih.sheets_sql.driver.SheetsMetaCache;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.aweorih.sheets_sql.TestingData.USE_DATABASE_VALUES;

public class UseTest implements TokenConsumerAdapter {

  private boolean successful = false;

  @Test
  public void test() throws SQLException {

    QueryRunner queryRunner = new QueryRunner(new SheetsMetaCache(true), TestingData.getUrlParameter(), this, true);

    queryRunner.runQuery(USE_DATABASE_VALUES.query, USE_DATABASE_VALUES.params);

    Assert.assertTrue(successful);
  }

  @Override
  public Optional<List<Map<String, Object>>> consumeUseToken(String useToken) {
    Assert.assertNotNull(useToken);
    this.successful = true;
    return Optional.empty();
  }
}

package com.aweorih.sheets_sql.driver;

import com.aweorih.sheets_sql.utils.Utils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SheetsMetaCache {

  private static final Logger LOGGER = LogManager.getLogger(SheetsMetaCache.class);

  private final Map<String, String> sheetsNameIdMapping;
  private final boolean             enabled;

  public SheetsMetaCache(boolean enabled) {
    this.enabled = enabled;
    if (enabled) sheetsNameIdMapping = new HashMap<>();
    else sheetsNameIdMapping = Collections.emptyMap();
  }

  public void addSheetsIdMapping(String name, String id) {
    if (!enabled) return;
    sheetsNameIdMapping.put(name, id);
  }

  public String getSheetsIdByNameOrDefault(String name, String other) {
    if (!enabled) return other;

    String opResponse = sheetsNameIdMapping.get(name);

    if (null == opResponse || opResponse.equals("null")) return other;

    return opResponse;
  }

  public Set<String> getKnownNames() {
    if (!enabled) return Collections.emptySet();
    return sheetsNameIdMapping.keySet();
  }

  @Override
  public String toString() {
    return "SheetsMetaCache{"
           + "\n"
           +
           "sheetsNameIdMapping='"
           + Utils.writeJsonWithDefaultSettings(sheetsNameIdMapping)
           + "',\n"
           +
           '}';
  }
}

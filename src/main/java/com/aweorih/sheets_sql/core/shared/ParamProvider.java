package com.aweorih.sheets_sql.core.shared;

import com.google.common.base.Verify;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class ParamProvider {

  private final List<String> params;
  private       int          pointer = 0;

  public ParamProvider(List<String> params) {
    this.params = ImmutableList.copyOf(params);
  }

  public String getParam() {
    Verify.verify(pointer < params.size());
    return params.get(pointer++);
  }
}

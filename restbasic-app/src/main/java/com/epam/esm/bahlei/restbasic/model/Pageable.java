package com.epam.esm.bahlei.restbasic.model;

import static java.lang.Math.*;

public class Pageable {
  private final int limit;
  private final int page;

  public Pageable(int page, int limit) {
    this.limit = max(limit, 0);
    this.page = page > 0 ? page : 1;
  }

  public int getOffset() {
    return limit * (page - 1);
  }

  public int getLimit() {
    return limit;
  }
}

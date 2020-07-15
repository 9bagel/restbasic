package com.epam.esm.bahlei.restbasic.model;

public class Pageable {
  private long offset;
  private long limit;
  private long page;

  public Pageable() {}

  public Pageable(long page, long limit) {
    this.limit = limit > 0 ? limit : 0;
    this.page = page > 0 ? page : 1;
    this.offset = this.limit * (this.page - 1);
  }

  public long getOffset() {
    return offset;
  }

  public long getLimit() {
    return limit;
  }

  public long getPage() {
    return page;
  }
}

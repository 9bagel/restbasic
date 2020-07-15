package com.epam.esm.bahlei.restbasic.model;

public class Pageable {
  private int offset;
  private int limit;
  private int page;

  public Pageable() {}

  public Pageable(int page, int limit) {
    this.limit = limit > 0 ? limit : 0;
    this.page = page > 0 ? page : 1;
    this.offset = this.limit * (this.page - 1);
  }

  public int getOffset() {
    return offset;
  }

  public int getLimit() {
    return limit;
  }

  public int getPage() {
    return page;
  }
}

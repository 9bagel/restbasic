package com.epam.esm.bahlei.restbasic.model;

import static java.lang.Math.*;

public class Pageable {
  private final int size;
  private final int page;

  public Pageable(int page, int size) {
    this.size = max(size, 1);
    this.page = page > 0 ? page : 1;
  }

  public int getOffset() {
    return size * (page - 1);
  }

  public int getSize() {
    return size;
  }
}

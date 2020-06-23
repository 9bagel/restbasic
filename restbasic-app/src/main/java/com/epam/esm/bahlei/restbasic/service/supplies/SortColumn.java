package com.epam.esm.bahlei.restbasic.service.supplies;

public class SortColumn {
  public final String columnName;
  public final SortOrder sortOrder;

  public SortColumn(String columnName, SortOrder sortOrder) {
    this.columnName = columnName;
    this.sortOrder = sortOrder;
  }
}

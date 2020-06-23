package com.epam.esm.bahlei.restbasic.service.supplies;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class Criteria {
  public final String tagName;
  public final String findPhrase;
  public final List<SortColumn> sortColumns;

  public Criteria(List<SortColumn> sortColumns, String tagName, String findPhrase) {
    this.sortColumns = ImmutableList.copyOf(sortColumns);
    this.tagName = tagName;
    this.findPhrase = findPhrase;
  }
}
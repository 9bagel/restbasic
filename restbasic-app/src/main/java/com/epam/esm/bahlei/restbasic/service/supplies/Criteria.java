package com.epam.esm.bahlei.restbasic.service.supplies;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class Criteria {
  public final List<String> tagNames;
  public final String findPhrase;
  public final List<SortColumn> sortColumns;

  public Criteria(List<SortColumn> sortColumns, List<String> tagNames, String findPhrase) {
    this.sortColumns = ImmutableList.copyOf(sortColumns);
    this.tagNames = tagNames;
    this.findPhrase = findPhrase;
  }
}
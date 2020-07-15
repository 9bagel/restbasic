package com.epam.esm.bahlei.restbasic.service.supplies;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static java.util.Collections.emptyList;

public class Criteria {
  public final List<String> tagNames;
  public final String findPhrase;
  public final List<SortColumn> sortColumns;

  public Criteria(List<SortColumn> sortColumns, List<String> tagNames, String findPhrase) {
    this.sortColumns = ImmutableList.copyOf(sortColumns);
    this.tagNames = tagNames == null ? emptyList() : tagNames;
    this.findPhrase = findPhrase;
  }
}

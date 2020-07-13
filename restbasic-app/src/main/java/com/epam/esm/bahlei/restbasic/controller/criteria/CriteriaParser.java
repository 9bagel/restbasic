package com.epam.esm.bahlei.restbasic.controller.criteria;

import com.epam.esm.bahlei.restbasic.service.supplies.Criteria;
import com.epam.esm.bahlei.restbasic.service.supplies.SortColumn;
import com.epam.esm.bahlei.restbasic.service.supplies.SortOrder;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class CriteriaParser {

  public Criteria parse(List<String> tagNames, String sortBy, String findPhrase) {
    List<SortColumn> sortColumns = new ArrayList<>();

    if (sortBy != null) {
      List<String> columnNames = asList(sortBy.split(","));

      columnNames.forEach(
          columnName -> sortColumns.add(parseSortColumn(columnName)));
    }
    return new Criteria(sortColumns, tagNames, findPhrase);
  }

  private SortColumn parseSortColumn(String columnName) {
    SortOrder sortOrder = columnName.startsWith("-") ? SortOrder.DESC : SortOrder.ASC;
    if (columnName.startsWith("-") || columnName.startsWith("+")) {
      columnName = columnName.substring(1);
    }
    return new SortColumn(columnName, sortOrder);
  }
}

package com.epam.esm.bahlei.restbasic.service.supplies;

import java.util.List;
import java.util.StringJoiner;

public class CriteriaToSQL {

  public static String mapSql(Criteria criteria) {
    StringBuilder sql = new StringBuilder();
    List<SortColumn> sortColumns = criteria.sortColumns;

    buildFilterSql(criteria.tagName, sql);
    buildSearchSql(criteria.findPhrase, sql);
    buildSortingSql(sortColumns, sql);

    return sql.toString();
  }

  private static void buildFilterSql(String tagName, StringBuilder sql) {
    if (tagName == null || tagName.isEmpty()) {
      return;
    }
    String filterSql =
        "JOIN certificate_tags ct ON c.id = ct.certificate_id "
            + "JOIN tags t ON t.id = ct.tag_id WHERE t.name LIKE '"
            + tagName
            + "' ";
    sql.append(filterSql);
  }

  private static void buildSearchSql(String search, StringBuilder sql) {
    if (search == null || search.isEmpty()) {
      return;
    }
    if (sql.length() > 0) {
      sql.append("AND c in(select search('").append(search).append("') ) ");
    } else {
      sql.append("WHERE c in(select search('").append(search).append("') ) ");
    }
  }

  private static void buildSortingSql(List<SortColumn> sortColumns, StringBuilder sql) {
    if (sortColumns.isEmpty()) {
      return;
    }
    StringJoiner joiner = new StringJoiner(", ");
    for (SortColumn column : sortColumns) {
      joiner.add(column.columnName + " " + column.sortOrder);
    }

    sql.append("ORDER BY ").append(joiner.toString());
  }
}

package com.epam.esm.bahlei.restbasic.service.supplies;

import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.google.common.base.Strings;
import org.hibernate.query.NativeQuery;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.StringJoiner;

public class CriteriaToSQL {

  private CriteriaToSQL() {
    throw new IllegalStateException("Utility class");
  }

  public static String mapSql(Criteria criteria) {
    StringBuilder sql = new StringBuilder();
    List<SortColumn> sortColumns = criteria.sortColumns;

    buildFilterSql(criteria.tagNames, sql);
    buildSearchSql(criteria.findPhrase, sql);
    buildSortingSql(sortColumns, sql);

    return sql.toString();
  }

  private static void buildFilterSql(List<String> tagNames, StringBuilder sql) {
    if (tagNames.isEmpty()) {
      return;
    }
    String filterSql =
        "JOIN certificate_tags ct ON c.id = ct.certificate_id "
            + "JOIN tags t ON t.id = ct.tag_id WHERE t.name IN :names "
            + "GROUP BY (c.id, ct.id, t.id) "
            + "HAVING COUNT(c.id) >= :size ";
    sql.append(filterSql);
  }

  private static void buildSearchSql(String search, StringBuilder sql) {
    if (search.isEmpty()) {
      return;
    }
    if (sql.length() > 0) {
      sql.append("AND c in(select search(:findPhrase))");
    } else {
      sql.append("WHERE c in(select search(:findPhrase))");
    }
  }

  private static void buildSortingSql(List<SortColumn> sortColumns, StringBuilder sql) {
    if (sortColumns.isEmpty()) {
      return;
    }
    StringJoiner joiner = new StringJoiner(", ");
    sortColumns.forEach(
        sortColumn -> {
          joiner.add("c." + sortColumn.columnName + " " + sortColumn.sortOrder);
        });

    sql.append("ORDER BY ").append(joiner.toString());
  }

  public static Query buildQuery(
      EntityManager entityManager, Criteria criteria, Pageable pageable) {

    String sql = CriteriaToSQL.mapSql(criteria);
    String wholeSql = "SELECT * FROM certificates c " + sql;

    Query query =
        entityManager
            .createNativeQuery(wholeSql, GiftCertificate.class)
            .setFirstResult(pageable.getOffset())
            .setMaxResults(pageable.getSize());
    ;

    if (!criteria.tagNames.isEmpty()) {
      query.setParameter("names", criteria.tagNames);
      query.setParameter("size", (long) criteria.tagNames.size());
    }

    if (!criteria.findPhrase.isEmpty()) {
      query.setParameter("findPhrase", criteria.findPhrase);
    }
    return query;
  }
}

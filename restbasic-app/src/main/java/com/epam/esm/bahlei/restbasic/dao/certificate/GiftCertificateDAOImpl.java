package com.epam.esm.bahlei.restbasic.dao.certificate;

import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.service.supplies.Criteria;
import com.epam.esm.bahlei.restbasic.service.supplies.CriteriaToSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDAOImpl implements GiftCertificateDAO {
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public GiftCertificateDAOImpl(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  private GiftCertificate toCertificate(ResultSet resultSet, int i) throws SQLException {
    GiftCertificate giftCertificate = new GiftCertificate();

    giftCertificate.setId(resultSet.getLong("id"));
    giftCertificate.setName(resultSet.getString("name"));
    giftCertificate.setCreatedAt(
        resultSet.getTimestamp("created_at").toInstant().atOffset(ZoneOffset.UTC));
    giftCertificate.setModifiedAt(
        resultSet.getTimestamp("updated_at").toInstant().atOffset(ZoneOffset.UTC));
    giftCertificate.setDuration(resultSet.getInt("duration"));
    giftCertificate.setPrice(resultSet.getBigDecimal("price"));
    giftCertificate.setDescription(resultSet.getString("description"));

    return giftCertificate;
  }

  @Override
  public List<GiftCertificate> getAll(Criteria criteria) {
    String sql = CriteriaToSQL.mapSql(criteria);
    String wholeSql =
        "SELECT c.id, c.name, c.description, c.price, c.created_at, c.updated_at, c.duration "
            + "FROM certificates c "
            + sql;

    return jdbcTemplate.query(wholeSql, this::toCertificate);
  }

  @Override
  public void save(GiftCertificate certificate) {
    String sql =
        "INSERT INTO certificates(name, description, price, created_at, updated_at, duration) "
            + "VALUES(?, ?, ?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        connection -> {
          PreparedStatement statement =
              connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
          statement.setString(1, certificate.getName());
          statement.setString(2, certificate.getDescription());
          statement.setBigDecimal(3, certificate.getPrice());
          statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
          statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
          statement.setInt(6, certificate.getDuration());
          return statement;
        },
        keyHolder);

    certificate.setId((Long) keyHolder.getKeys().get("id"));
  }

  @Override
  public Optional<GiftCertificate> get(long id) {
    String sql =
        "SELECT id, name, description, price, created_at, updated_at, duration "
            + "FROM certificates WHERE id = ?";
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(sql, new Object[] {id}, this::toCertificate));
    } catch (EmptyResultDataAccessException ex) {
      return Optional.empty();
    }
  }

  @Override
  public void delete(long id) {
    String sql = "DELETE FROM certificates WHERE id = ?";

    jdbcTemplate.update(sql, id);
  }

  @Override
  public void update(GiftCertificate certificate) {
    String sql =
        "UPDATE certificates SET name = ?, description = ?, price = ?, "
            + "updated_at = ?, duration = ? WHERE id = ?";

    jdbcTemplate.update(
        sql,
        certificate.getName(),
        certificate.getDescription(),
        certificate.getPrice(),
        Timestamp.valueOf(LocalDateTime.now()),
        certificate.getDuration(),
        certificate.getId());
  }

  @Override
  public Optional<GiftCertificate> getByName(String name) {
    String sql =
        "SELECT id, name, description, price, created_at, updated_at, duration "
            + "FROM certificates WHERE name = ?";
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(sql, new Object[] {name}, this::toCertificate));
    } catch (EmptyResultDataAccessException ex) {
      return Optional.empty();
    }
  }

  @Override
  public List<GiftCertificate> getOrderedCertificates(long id) {
    String sql =
        "SELECT c.id, c.name, c.description, c.price, c.created_at, c.updated_at, c.duration "
            + "FROM certificates c "
            + "JOIN ordered_certificates ON c.id = ordered_certificates.certificate_id "
            + "WHERE ordered_certificates.order_id = ?";

    return jdbcTemplate.query(sql, new Object[] {id}, this::toCertificate);
  }

  @Override
  public Optional<GiftCertificate> getFavouriteUserCertificate(long userId) {
    String sql =
        "SELECT c.id, c.name, c.description, c.price, c.created_at, c.updated_at, c.duration, "
            + "COUNT(c.id) AS \"count\" "
            + "FROM certificates c "
            + "JOIN ordered_certificates oc ON c.id = oc.certificate_id "
            + "JOIN user_orders uo ON oc.order_id = uo.order_id "
            + "WHERE uo.user_id = ? "
            + "GROUP BY c.id "
            + "ORDER BY \"count\" DESC "
            + "LIMIT 1";

    return Optional.ofNullable(
        jdbcTemplate.queryForObject(sql, new Object[] {userId}, this::toCertificate));
  }
}

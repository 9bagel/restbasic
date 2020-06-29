package com.epam.esm.bahlei.restbasic.dao.order;

import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;

@Repository
public class OrderDAOImpl implements OrderDAO {
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public OrderDAOImpl(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  private Order toOrder(ResultSet resultSet, int i) throws SQLException {
    Order order = new Order();

    order.setId(resultSet.getLong("id"));
    order.setCost(resultSet.getBigDecimal("cost"));
    order.setPurchaseDate(resultSet.getTimestamp("purchase_date").toInstant().atOffset(UTC));

    return order;
  }

  @Override
  public void save(Order order) {
    String sql = "INSERT INTO orders(cost, purchase_date) VALUES(?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        connection -> {
          PreparedStatement statement =
              connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
          statement.setBigDecimal(1, order.getCost());
          statement.setTimestamp(2, Timestamp.from(order.getPurchaseDate().toInstant()));
          return statement;
        },
        keyHolder);
    order.setId((Long) keyHolder.getKeys().get("id"));
  }

  @Override
  public List<Order> getAll() {
    String sql = "SELECT id, cost, purchase_date FROM orders";

    return jdbcTemplate.query(sql, this::toOrder);
  }

  @Override
  public Optional<Order> get(long id) {
    String sql = "SELECT id, cost, purchase_date FROM orders WHERE id = ?";
    try {
      return Optional.ofNullable(
              jdbcTemplate.queryForObject(sql, new Object[] {id}, this::toOrder));
    } catch (EmptyResultDataAccessException ex) {
      return Optional.empty();
    }
  }

  @Override
  public void saveUserOrder(long userId, long orderId) {
    String sql = "INSERT INTO user_orders(user_id, order_id) VALUES(?, ?)";

    jdbcTemplate.update(sql, userId, orderId);
  }

  @Override
  public List<Order> getUserOrders(long userId) {
    String sql = "SELECT o.id, o.cost, o.purchase_date FROM orders o " +
            "JOIN user_orders ON o.id = user_orders.order_id WHERE user_orders.user_id = ?";

    return jdbcTemplate.query(sql, new Object[] {userId}, this::toOrder);
  }

  @Override
  public Optional<Order> getUserOrderDetails(long userId, long orderId) {
    String sql = "SELECT o.id, o.cost, o.purchase_date " +
            "FROM orders o " +
            "JOIN user_orders " +
            "ON o.id = user_orders.order_id " +
            "WHERE user_orders.user_id = ? " +
            "AND user_orders.order_id = ?";
    return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[] {userId, orderId}, this::toOrder));
  }

  @Override
  public void saveOrderedCertificates(List<GiftCertificate> certificates, long orderId) {
    String sql = "INSERT INTO ordered_certificates (certificate_id, order_id) VALUES (?, ?)";

    certificates.forEach(certificate -> jdbcTemplate.update(sql, certificate.getId(), orderId));
  }

}

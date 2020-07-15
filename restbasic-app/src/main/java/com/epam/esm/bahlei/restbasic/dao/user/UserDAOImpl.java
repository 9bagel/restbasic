package com.epam.esm.bahlei.restbasic.dao.user;

import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public UserDAOImpl(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  private User toUser(ResultSet resultSet, int i) throws SQLException {
    User user = new User();

    user.setId(resultSet.getLong("id"));
    user.setFirstName(resultSet.getString("first_name"));
    user.setSecondName(resultSet.getString("second_name"));

    return user;
  }

  @Override
  public List<User> getAll(Pageable pageable) {
    String sql = "SELECT id, first_name, second_name FROM users LIMIT ? OFFSET ? ";

    return jdbcTemplate.query(
        sql, new Object[] {pageable.getLimit(), pageable.getOffset()}, this::toUser);
  }

  @Override
  public Optional<User> get(long id) {
    String sql = "SELECT id, first_name, second_name FROM users WHERE id = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[] {id}, this::toUser));
    } catch (EmptyResultDataAccessException ex) {
      return Optional.empty();
    }
  }

  @Override
  public void save(User user) {
    String sql = "INSERT INTO users(first_name, second_name) VALUES(?, ?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        connection -> {
          PreparedStatement statement =
              connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
          statement.setString(1, user.getFirstName());
          statement.setString(2, user.getSecondName());
          return statement;
        },
        keyHolder);
    user.setId((Long) keyHolder.getKeys().get("id"));
  }
}

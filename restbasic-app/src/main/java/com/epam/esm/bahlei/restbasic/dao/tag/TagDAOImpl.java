package com.epam.esm.bahlei.restbasic.dao.tag;

import com.epam.esm.bahlei.restbasic.model.Tag;
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
public class TagDAOImpl implements TagDAO {
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public TagDAOImpl(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  private Tag toTag(ResultSet resultSet, int i) throws SQLException {
    Tag tag = new Tag();

    tag.setId(resultSet.getLong("id"));
    tag.setName(resultSet.getString("name"));

    return tag;
  }

  @Override
  public List<Tag> getAll(int limit, long offset) {
    String sql = "SELECT id, name FROM tags LIMIT ? OFFSET ? ";

    return jdbcTemplate.query(sql, new Object[] {limit, offset}, this::toTag);
  }

  @Override
  public void save(Tag tag) {
    String sql = "INSERT INTO tags(name) VALUES(?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        connection -> {
          PreparedStatement statement =
              connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
          statement.setString(1, tag.getName());
          return statement;
        },
        keyHolder);
    tag.setId((Long) keyHolder.getKeys().get("id"));
  }

  @Override
  public Optional<Tag> get(long tagId) {
    String sql = "SELECT id, name FROM tags WHERE id = ?";
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(sql, new Object[] {tagId}, this::toTag));
    } catch (EmptyResultDataAccessException ex) {
      return Optional.empty();
    }
  }

  @Override
  public void delete(long tagId) {
    String sql = "DELETE FROM tags WHERE id = ?";

    jdbcTemplate.update(sql, tagId);
  }

  @Override
  public Optional<Tag> getByName(String tagName) {
    String sql = "SELECT id, name FROM tags WHERE name = ?";
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(sql, new Object[] {tagName}, this::toTag));
    } catch (EmptyResultDataAccessException ex) {
      return Optional.empty();
    }
  }

  @Override
  public void saveCertificateTag(long giftCertificateId, long tagId) {
    String sql = "INSERT INTO certificate_tags (certificate_id, tag_id) VALUES (?, ?)";

    jdbcTemplate.update(sql, giftCertificateId, tagId);
  }

  @Override
  public List<Tag> getCertificateTags(long certificateId) {
    String sql =
        "SELECT tags.id, tags.name FROM tags JOIN certificate_tags ON tags.id = certificate_tags.tag_id "
            + "WHERE certificate_tags.certificate_id = ?";

    return jdbcTemplate.query(sql, new Object[] {certificateId}, this::toTag);
  }

  @Override
  public void deleteCertificateTags(long certificateId) {
    String sql = "DELETE FROM certificate_tags WHERE certificate_id = ?";

    jdbcTemplate.update(sql, certificateId);
  }
}

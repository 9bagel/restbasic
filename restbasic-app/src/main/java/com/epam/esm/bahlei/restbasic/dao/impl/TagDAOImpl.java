package com.epam.esm.bahlei.restbasic.dao.impl;

import com.epam.esm.bahlei.restbasic.dao.TagDAO;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDAOImpl implements TagDAO {
  private final EntityManager entityManager;

  @Autowired
  public TagDAOImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public List<Tag> getAll(Pageable pageable) {
    TypedQuery<Tag> query =
        entityManager
            .createQuery("FROM Tag", Tag.class)
            .setFirstResult(pageable.getOffset())
            .setMaxResults(pageable.getLimit());

    return query.getResultList();
  }

  @Override
  public void save(Tag tag) {
    Tag mergedTag = entityManager.merge(tag);
    tag.setId(mergedTag.getId());
  }

  @Override
  public Optional<Tag> get(long tagId) {
    return Optional.ofNullable(entityManager.find(Tag.class, tagId));
  }

  @Override
  @Transactional
  public void delete(long tagId) {
    Tag tag = entityManager.find(Tag.class, tagId);
    entityManager.remove(tag);
  }

  @Override
  public Optional<Tag> getByName(String tagName) {
    TypedQuery<Tag> query = entityManager.createQuery("from Tag where name=:tagName", Tag.class);
    query.setParameter("tagName", tagName);

    return query.getResultList().stream().findFirst();
  }

  @Override
  public void saveCertificateTag(long certificateId, long tagId) {
    String sql =
        "INSERT INTO certificate_tags (certificate_id, tag_id) VALUES (:certificateId, :tagId)";

    Query query = entityManager.createNativeQuery(sql);
    query.setParameter("certificateId", certificateId);
    query.setParameter("tagId", tagId);

    query.executeUpdate();
  }

  @Override
  public List<Tag> getCertificateTags(long certificateId) {
    String sql =
        "SELECT tags.id, tags.name FROM tags JOIN certificate_tags ON tags.id = certificate_tags.tag_id "
            + "WHERE certificate_tags.certificate_id = :certificateId";

    Query query = entityManager.createNativeQuery(sql);
    query.setParameter("certificateId", certificateId);

    return query.getResultList();
  }

  @Override
  public void deleteCertificateTags(long certificateId) {
    String sql = "DELETE FROM certificate_tags WHERE certificate_id = :certificateId";

    Query query = entityManager.createNativeQuery(sql);
    query.setParameter("certificateId", certificateId);

    query.executeUpdate();
  }
}

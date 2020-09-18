package com.epam.esm.bahlei.restbasic.dao.impl;

import com.epam.esm.bahlei.restbasic.dao.GiftCertificateDAO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.service.supplies.Criteria;
import com.epam.esm.bahlei.restbasic.service.supplies.CriteriaToSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDAOImpl implements GiftCertificateDAO {
  private final EntityManager entityManager;

  @Autowired
  public GiftCertificateDAOImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public List<GiftCertificate> getAll(Criteria criteria, Pageable pageable) {
    Query query = CriteriaToSQL.buildQuery(entityManager, criteria, pageable);

    return query.getResultList();
  }

  @Override
  public void save(GiftCertificate certificate) {
    GiftCertificate mergedCertificate = entityManager.merge(certificate);
    certificate.setId(mergedCertificate.getId());
  }

  @Override
  public Optional<GiftCertificate> get(long id) {
    TypedQuery<GiftCertificate> query =
        entityManager.createQuery("from GiftCertificate where id = :id", GiftCertificate.class);
    query.setParameter("id", id);

    return query.getResultList().stream().findFirst();
  }

  @Override
  public void delete(long id) {
    Query query = entityManager.createQuery("delete from GiftCertificate where id=:id");

    query.setParameter("id", id);
    query.executeUpdate();
  }

  @Override
  public void update(GiftCertificate certificate) {
    GiftCertificate dbCertificate = entityManager.merge(certificate);
    certificate.setId(dbCertificate.getId());
  }

  @Override
  public Optional<GiftCertificate> getByName(String name) {
    TypedQuery<GiftCertificate> query =
        entityManager.createQuery("from GiftCertificate where name=:name", GiftCertificate.class);
    query.setParameter("name", name);

    return query.getResultList().stream().findFirst();
  }

  @Override
  public List<GiftCertificate> getCertificatesByOrderId(long orderId) {
    TypedQuery<GiftCertificate> query =
        entityManager.createQuery(
            "SELECT c from Order o JOIN o.certificates c WHERE o.id = :orderId",
            GiftCertificate.class);

    query.setParameter("orderId", orderId);

    return query.getResultList();
  }

  @Override
  public Optional<GiftCertificate> getFavouriteUserCertificate(long userId) {

    TypedQuery<GiftCertificate> query =
        entityManager
            .createQuery(
                "SELECT c FROM Order o JOIN o.certificates c "
                    + "WHERE o.userId = ?1 "
                    + "GROUP BY c.id "
                    + "ORDER BY COUNT(c.id) DESC",
                GiftCertificate.class)
            .setMaxResults(1)
            .setParameter(1, userId);

    return query.getResultList().stream().findFirst();
  }
}

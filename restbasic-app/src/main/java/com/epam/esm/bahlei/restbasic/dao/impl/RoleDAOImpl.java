package com.epam.esm.bahlei.restbasic.dao.impl;

import com.epam.esm.bahlei.restbasic.dao.RoleDAO;
import com.epam.esm.bahlei.restbasic.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class RoleDAOImpl implements RoleDAO {
  private final EntityManager entityManager;

  @Autowired
  public RoleDAOImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Optional<Role> getByName(String name) {
    TypedQuery<Role> query =
        entityManager
            .createQuery("FROM Role r WHERE r.name = ?1", Role.class)
            .setParameter(1, name);

    return query.getResultList().stream().findFirst();
  }
}

package com.epam.esm.bahlei.restbasic.dao.impl;

import com.epam.esm.bahlei.restbasic.dao.UserDAO;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {
  private final EntityManager entityManager;

  @Autowired
  public UserDAOImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public List<User> getAll(Pageable pageable) {
    TypedQuery<User> query =
        entityManager
            .createQuery("FROM User", User.class)
            .setFirstResult(pageable.getOffset())
            .setMaxResults(pageable.getLimit());

    return query.getResultList();
  }

  @Override
  public Optional<User> get(long id) {
    return Optional.ofNullable(entityManager.find(User.class, id));
  }

  @Override
  public void save(User user) {
    entityManager.persist(user);
  }

  @Override
  public Optional<User> getByUsername(String username) {
    TypedQuery<User> query =
        entityManager
            .createQuery("FROM User u WHERE u.username = ?1", User.class)
            .setParameter(1, username);

    return query.getResultList().stream().findFirst();
  }
}

package com.epam.esm.bahlei.restbasic.dao.order;

import com.epam.esm.bahlei.restbasic.model.Order;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDAOImpl implements OrderDAO {
  private final EntityManager entityManager;

  @Autowired
  public OrderDAOImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public void save(Order order) {
    order.setPurchasedAt(Instant.now());
    entityManager.persist(order);
  }

  @Override
  public Optional<Order> get(long id) {
    return Optional.ofNullable(entityManager.find(Order.class, id));
  }

  @Override
  public List<Order> getUserOrders(long id, Pageable pageable) {
    TypedQuery<Order> query =
        entityManager
            .createQuery("SELECT o FROM Order o WHERE o.userId = ?1", Order.class)
            .setFirstResult(pageable.getOffset())
            .setMaxResults(pageable.getLimit());

    query.setParameter(1, id);

    return query.getResultList();
  }

  @Override
  public Optional<Order> getUserOrderDetails(long userId, long orderId) {
    TypedQuery<Order> query =
        entityManager
            .createQuery("SELECT o FROM Order o WHERE o.userId = ?1 AND o.id = ?2", Order.class)
            .setParameter(1, userId)
            .setParameter(2, orderId);

    return query.getResultList().stream().findFirst();
  }
}

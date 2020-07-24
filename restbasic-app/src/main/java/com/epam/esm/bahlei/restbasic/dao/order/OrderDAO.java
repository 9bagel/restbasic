package com.epam.esm.bahlei.restbasic.dao.order;

import com.epam.esm.bahlei.restbasic.model.Order;
import com.epam.esm.bahlei.restbasic.model.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderDAO {

  void save(Order order);

  Optional<Order> get(long id);

  List<Order> getUserOrders(long id, Pageable pageable);

  Optional<Order> getUserOrderDetails(long userId, long orderId);
}

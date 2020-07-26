package com.epam.esm.bahlei.restbasic.dao;

import com.epam.esm.bahlei.restbasic.model.Order;
import com.epam.esm.bahlei.restbasic.model.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderDAO {

  void save(Order order);

  List<Order> getUserOrders(long id, Pageable pageable);

  Optional<Order> get(long userId, long orderId);
}

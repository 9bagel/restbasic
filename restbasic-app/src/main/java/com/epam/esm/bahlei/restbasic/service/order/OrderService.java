package com.epam.esm.bahlei.restbasic.service.order;

import com.epam.esm.bahlei.restbasic.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

  void save(Order order, long userId);

  List<Order> getAll();

  Optional<Order> get(long orderId, long userId);

  List<Order> getUserOrders(long userId);
}

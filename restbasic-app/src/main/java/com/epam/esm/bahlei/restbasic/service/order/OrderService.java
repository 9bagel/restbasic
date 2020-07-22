package com.epam.esm.bahlei.restbasic.service.order;

import com.epam.esm.bahlei.restbasic.model.Order;
import com.epam.esm.bahlei.restbasic.model.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {

  void save(Order order);

  Optional<Order> get(long orderId, long userId);

  List<Order> getUserOrders(long id, Pageable pageable);

  Optional<Order> get(long orderId);
}

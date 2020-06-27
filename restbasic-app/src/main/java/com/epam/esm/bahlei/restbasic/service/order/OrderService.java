package com.epam.esm.bahlei.restbasic.service.order;

import com.epam.esm.bahlei.restbasic.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

  void save(Order order);

  List<Order> getAll();

  Optional<Order> get(long id);
}

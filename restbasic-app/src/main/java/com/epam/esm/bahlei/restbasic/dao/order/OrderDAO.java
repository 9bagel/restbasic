package com.epam.esm.bahlei.restbasic.dao.order;

import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDAO {

  void save(Order order);

  void saveOrderedCertificates(List<GiftCertificate> certificates, long orderId);

  List<Order> getAll();

  Optional<Order> get(long id);

  List<Order> getUserOrders(long userId);

  void saveUserOrder(long userId, long orderId);

    Optional<Order> getUserOrderDetails(long userId, long orderId);
}

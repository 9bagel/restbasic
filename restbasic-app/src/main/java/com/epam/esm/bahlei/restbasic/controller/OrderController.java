package com.epam.esm.bahlei.restbasic.controller;

import com.epam.esm.bahlei.restbasic.model.Order;
import com.epam.esm.bahlei.restbasic.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/orders/")
public class OrderController {
  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<?> getOrder(@PathVariable long orderId) {

    Optional<Order> optional = orderService.get(orderId);
    if (!optional.isPresent()) {
      return status(HttpStatus.NOT_FOUND).build();
    }

    return ok(optional.get());
  }
}

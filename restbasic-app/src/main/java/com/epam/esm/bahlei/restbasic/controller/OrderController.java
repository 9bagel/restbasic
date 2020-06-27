package com.epam.esm.bahlei.restbasic.controller;

import com.epam.esm.bahlei.restbasic.controller.dto.OrderDTO;
import com.epam.esm.bahlei.restbasic.model.Order;
import com.epam.esm.bahlei.restbasic.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping("/")
  public ResponseEntity<?> createOrder(
      @RequestBody OrderDTO orderDTO, HttpServletRequest httpServletRequest) {
    Order order = toOrder(orderDTO);

    orderService.save(order);

    return created(URI.create(httpServletRequest.getRequestURL().append(order.getId()).toString()))
        .build();
  }

  @GetMapping("/")
  public ResponseEntity<?> getAll() {

    return ok(orderService.getAll().stream().map(this::toOrderDTO).collect(toList()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getUser(@PathVariable long id) {
    Optional<Order> optional = orderService.get(id);
    if (!optional.isPresent()) {
      return status(HttpStatus.NOT_FOUND).build();
    }

    return ok(optional.get());
  }

  private Order toOrder(OrderDTO orderDTO) {
    Order order = new Order();
    order.setId(orderDTO.id);
    order.setCost(orderDTO.cost);
    order.setPurchaseDate(orderDTO.purchaseDate);
    order.setCertificates(orderDTO.certificates);
    return order;
  }

  private OrderDTO toOrderDTO(Order order) {

    return new OrderDTO(order);
  }
}

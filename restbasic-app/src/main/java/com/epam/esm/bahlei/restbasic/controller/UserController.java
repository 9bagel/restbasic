package com.epam.esm.bahlei.restbasic.controller;

import com.epam.esm.bahlei.restbasic.controller.dto.OrderDTO;
import com.epam.esm.bahlei.restbasic.controller.dto.UserDTO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Order;
import com.epam.esm.bahlei.restbasic.model.User;
import com.epam.esm.bahlei.restbasic.service.certificate.GiftCertificateService;
import com.epam.esm.bahlei.restbasic.service.order.OrderService;
import com.epam.esm.bahlei.restbasic.service.user.UserService;
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
@RequestMapping("/api/users/")
public class UserController {
  private final UserService userService;
  private final OrderService orderService;
  private final GiftCertificateService certificateService;

  @Autowired
  public UserController(
      UserService userService,
      OrderService orderService,
      GiftCertificateService certificateService) {
    this.userService = userService;
    this.orderService = orderService;
    this.certificateService = certificateService;
  }

  @PostMapping("/")
  public ResponseEntity<?> addUser(
      @RequestBody UserDTO userDTO, HttpServletRequest httpServletRequest) {
    User user = toUser(userDTO);

    userService.save(user);

    return created(URI.create(httpServletRequest.getRequestURL().append(user.getId()).toString()))
        .build();
  }

  @GetMapping("/")
  public ResponseEntity<?> getAll() {

    return ok(userService.getAll().stream().map(this::toUserDTO).collect(toList()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getUser(@PathVariable long id) {
    Optional<User> optional = userService.get(id);
    if (!optional.isPresent()) {
      return status(HttpStatus.NOT_FOUND).build();
    }

    return ok(toUserDTO(optional.get()));
  }

  @GetMapping("/{userId}/orders")
  public ResponseEntity<?> getUserOrders(@PathVariable long userId) {
    return ok(orderService.getUserOrders(userId));
  }

  @GetMapping("/{userId}/orders/favourite_certificate")
  public ResponseEntity<?> getFavouriteCertificate(@PathVariable long userId) {
    Optional<GiftCertificate> optional = certificateService.getFavouriteUserCertificate(userId);
    if (!optional.isPresent()) {
      return status(HttpStatus.NOT_FOUND).build();
    }
    return ok(optional.get());
  }

  @GetMapping("/{userId}/orders/{orderId}")
  public ResponseEntity<?> getOrder(@PathVariable long userId, @PathVariable long orderId) {
    Optional<Order> optional = orderService.get(orderId, userId);
    if (!optional.isPresent()) {
      return status(HttpStatus.NOT_FOUND).build();
    }

    return ok(optional.get());
  }

  @PostMapping("/{userId}/orders")
  public ResponseEntity<?> createOrder(
      @PathVariable long userId,
      @RequestBody OrderDTO orderDTO,
      HttpServletRequest httpServletRequest) {
    Order order = toOrder(orderDTO);

    orderService.save(order, userId);

    return created(URI.create(httpServletRequest.getRequestURL().append(order.getId()).toString()))
        .build();
  }

  private UserDTO toUserDTO(User user) {

    return new UserDTO(user);
  }

  private User toUser(UserDTO userDTO) {
    User user = new User();
    user.setId(userDTO.id);
    user.setFirstName(userDTO.firstName);
    user.setSecondName(userDTO.secondName);
    return user;
  }

  private Order toOrder(OrderDTO orderDTO) {
    Order order = new Order();
    order.setId(orderDTO.id);
    order.setCost(orderDTO.cost);
    order.setPurchaseDate(orderDTO.purchaseDate);
    order.setCertificates(orderDTO.certificates);
    return order;
  }
}

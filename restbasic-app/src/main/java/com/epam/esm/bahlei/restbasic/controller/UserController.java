package com.epam.esm.bahlei.restbasic.controller;

import com.epam.esm.bahlei.restbasic.controller.dto.OrderDTO;
import com.epam.esm.bahlei.restbasic.controller.dto.UserDTO;
import com.epam.esm.bahlei.restbasic.controller.refdto.RefDTO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Order;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.User;
import com.epam.esm.bahlei.restbasic.service.certificate.GiftCertificateService;
import com.epam.esm.bahlei.restbasic.service.order.OrderService;
import com.epam.esm.bahlei.restbasic.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/api/users")
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
  // Заменить refDTO на DTO +
  @GetMapping("/{userId}")
  public ResponseEntity<?> getUser(@PathVariable long userId) {
    Optional<User> optional = userService.get(userId);
    if (!optional.isPresent()) {
      return status(HttpStatus.NOT_FOUND).build();
    }
    Link selfLink = linkTo(methodOn(UserController.class).getUser(userId)).withSelfRel();
    Link orders =
        linkTo(methodOn(UserController.class).getUserOrders(userId, 1, 10)).withRel("orders");

    UserDTO userDTO = toUserDTO(optional.get());
    userDTO.add(selfLink, orders);
    return ok(userDTO);
  }
  // Заменить refDTO на DTO +
  @GetMapping("/{userId}/orders")
  public ResponseEntity<?> getUserOrders(
      @PathVariable long userId,
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    List<Order> userOrders = orderService.getUserOrders(userId, new Pageable(page, size));
    List<OrderDTO> orderDTOs =
        userOrders.stream()
            .map(this::toOrderDTO)
            .map(
                orderDTO ->
                    orderDTO.add(
                        linkTo(methodOn(UserController.class).getOrder(userId, orderDTO.id))
                            .withSelfRel()))
            .collect(toList());

    orderDTOs.forEach(
        orderDTO ->
            orderDTO.certificates.forEach(
                certificateRef ->
                    certificateRef.add(
                        linkTo(
                                methodOn(CertificateController.class)
                                    .getCertificate(certificateRef.getId()))
                            .withSelfRel())));
    return ok(orderDTOs);
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
    OrderDTO orderDTO = toOrderDTO(optional.get());
    Link selfLink = linkTo(methodOn(UserController.class).getOrder(userId, orderId)).withSelfRel();
    orderDTO.add(selfLink);

    return ok(orderDTO);
  }

  @PostMapping("/{userId}/orders")
  public ResponseEntity<?> createOrder(
      @PathVariable long userId, @RequestBody Order order, HttpServletRequest httpServletRequest) {
    order.setUserId(userId);

    orderService.save(order);

    return created(URI.create(httpServletRequest.getRequestURL().append(order.getId()).toString()))
        .build();
  }

  private UserDTO toUserDTO(User user) {

    return new UserDTO(user);
  }

  private RefDTO toRefDTO(User user) {

    return new RefDTO(user);
  }

  private User toUser(UserDTO userDTO) {
    User user = new User();
    user.setId(userDTO.id);
    user.setFirstName(userDTO.firstName);
    user.setSecondName(userDTO.secondName);
    return user;
  }

  private OrderDTO toOrderDTO(Order order) {
    return new OrderDTO(order);
  }
}

package com.epam.esm.bahlei.restbasic.controller;

import com.epam.esm.bahlei.restbasic.controller.dto.GiftCertificateDTO;
import com.epam.esm.bahlei.restbasic.controller.dto.OrderDTO;
import com.epam.esm.bahlei.restbasic.controller.dto.RefDTO;
import com.epam.esm.bahlei.restbasic.controller.dto.UserDTO;
import com.epam.esm.bahlei.restbasic.controller.linkmapper.LinkMapper;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Order;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.User;
import com.epam.esm.bahlei.restbasic.service.GiftCertificateService;
import com.epam.esm.bahlei.restbasic.service.OrderService;
import com.epam.esm.bahlei.restbasic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.*;
import static org.springframework.util.CollectionUtils.isEmpty;

@RestController
@RequestMapping("/api/")
public class UserController {
  private final UserService userService;
  private final OrderService orderService;
  private final GiftCertificateService certificateService;
  private final LinkMapper linkMapper;

  @Autowired
  public UserController(
      UserService userService,
      OrderService orderService,
      GiftCertificateService certificateService,
      LinkMapper linkMapper) {
    this.userService = userService;
    this.orderService = orderService;
    this.certificateService = certificateService;
    this.linkMapper = linkMapper;
  }

  @PreAuthorize("hasAuthority('USER')")
  @GetMapping("/users/{userId}")
  public ResponseEntity<?> getUser(@PathVariable long userId) {
    Optional<User> optional = userService.get(userId);
    if (!optional.isPresent()) {
      return status(HttpStatus.NOT_FOUND).build();
    }

    UserDTO userDTO = toUserDTO(optional.get());
    linkMapper.mapLinks(userDTO);
    return ok(userDTO);
  }

  @PreAuthorize("permitAll()")
  @PostMapping("/users/")
  public ResponseEntity<?> register(
      @RequestBody UserDTO userDTO, HttpServletRequest httpServletRequest) {
    User user = toUser(userDTO);

    userService.register(user);

    return created(URI.create(httpServletRequest.getRequestURL().append(user.getId()).toString()))
        .build();
  }

  @PreAuthorize("hasAuthority('USER')")
  @GetMapping("/users/{userId}/orders")
  public ResponseEntity<?> getUserOrders(
      @PathVariable long userId,
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    List<Order> userOrders = orderService.getUserOrders(userId, new Pageable(page, size));
    List<OrderDTO> orderDTOs =
        userOrders.stream().map(this::toOrderDTO).peek(linkMapper::mapLinks).collect(toList());

    return ok(orderDTOs);
  }

  @PreAuthorize("hasAuthority('USER')")
  @GetMapping("/users/{userId}/orders/favourite-certificate")
  public ResponseEntity<?> getFavouriteCertificate(@PathVariable long userId) {
    Optional<GiftCertificate> optional = certificateService.getFavouriteUserCertificate(userId);
    if (!optional.isPresent()) {
      return status(HttpStatus.NOT_FOUND).build();
    }
    return ok(new GiftCertificateDTO(optional.get()));
  }

  @PreAuthorize("hasAuthority('USER')")
  @GetMapping("/users/{userId}/orders/{orderId}")
  public ResponseEntity<?> getOrder(@PathVariable long userId, @PathVariable long orderId) {
    Optional<Order> optional = orderService.get(orderId, userId);
    if (!optional.isPresent()) {
      return status(HttpStatus.NOT_FOUND).build();
    }
    OrderDTO orderDTO = toOrderDTO(optional.get());
    linkMapper.mapLinks(orderDTO);

    return ok(orderDTO);
  }

  @PreAuthorize("hasAuthority('USER')")
  @PostMapping("/users/{userId}/orders")
  public ResponseEntity<?> createOrder(@PathVariable long userId, @RequestBody OrderDTO orderDTO) {
    Order order = toOrder(orderDTO);
    order.setUserId(userId);

    orderService.save(order);

    return created(linkTo(methodOn(UserController.class).getOrder(userId, order.getId())).toUri())
        .build();
  }

  private UserDTO toUserDTO(User user) {

    return new UserDTO(user);
  }

  private RefDTO toRefDTO(User user) {

    return new RefDTO(user.getId(), user.getFirstName() + " " + user.getLastName());
  }

  private OrderDTO toOrderDTO(Order order) {
    return new OrderDTO(order);
  }

  private User toUser(UserDTO userDTO) {
    User user = new User();
    user.setUsername(userDTO.username);
    user.setPassword(userDTO.password);
    user.setFirstName(userDTO.firstName);
    user.setLastName(userDTO.lastName);
    user.setEmail(userDTO.email);
    return user;
  }

  private Order toOrder(OrderDTO orderDTO) {
    Order order = new Order();
    order.setUserId(orderDTO.userId);

    if (!isEmpty(orderDTO.certificates))
      order.setCertificates(
          orderDTO.certificates.stream()
              .map(
                  certificateDto -> {
                    GiftCertificate certificate = new GiftCertificate();
                    certificate.setId(certificateDto.id);
                    return certificate;
                  })
              .collect(toList()));

    return order;
  }
}

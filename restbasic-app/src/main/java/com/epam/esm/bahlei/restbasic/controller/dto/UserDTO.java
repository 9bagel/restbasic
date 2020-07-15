package com.epam.esm.bahlei.restbasic.controller.dto;

import com.epam.esm.bahlei.restbasic.model.Order;
import com.epam.esm.bahlei.restbasic.model.User;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

import static java.util.Collections.emptyList;

public class UserDTO extends RepresentationModel<UserDTO> {
  public long id;
  public String firstName;
  public String secondName;

  public List<Order> orders;

  public UserDTO() {}

  public UserDTO(User user) {
    this.id = user.getId();
    this.firstName = user.getFirstName();
    this.secondName = user.getSecondName();
    this.orders = user.getOrders() == null ? emptyList() : user.getOrders();
  }
}
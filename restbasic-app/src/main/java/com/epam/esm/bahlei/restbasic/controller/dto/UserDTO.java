package com.epam.esm.bahlei.restbasic.controller.dto;

import com.epam.esm.bahlei.restbasic.model.Order;
import com.epam.esm.bahlei.restbasic.model.User;

import java.util.List;

public class UserDTO {
  public long id;
  public String firstName;
  public String secondName;

  public List<Order> orders;

  public UserDTO() {}

  public UserDTO(User user) {
    this.id = user.getId();
    this.firstName = user.getFirstName();
    this.secondName = user.getSecondName();
  }
}

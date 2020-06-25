package com.epam.esm.bahlei.restbasic.controller.dto;

import com.epam.esm.bahlei.restbasic.model.Order;

import java.util.List;

public class UserDTO {
  private long id;
  private String firstName;
  private String secondName;

  private List<Order> orders;
}

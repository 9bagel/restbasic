package com.epam.esm.bahlei.restbasic.model;

import java.util.List;

public class User {
  private long id;
  private String firstName;
  private String secondName;

  private List<Order> orders;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getSecondName() {
    return secondName;
  }

  public void setSecondName(String secondName) {
    this.secondName = secondName;
  }

  public List<Order> getOrders() {
    return orders;
  }
}

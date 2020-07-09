package com.epam.esm.bahlei.restbasic.controller.refdto;

import com.epam.esm.bahlei.restbasic.model.User;

public class UserRefDTO {
  public long id;
  public String firstName;
  public String secondName;

  public UserRefDTO() {}

  public UserRefDTO(User user) {
    this.id = user.getId();
    this.firstName = user.getFirstName();
    this.secondName = user.getSecondName();
  }
}

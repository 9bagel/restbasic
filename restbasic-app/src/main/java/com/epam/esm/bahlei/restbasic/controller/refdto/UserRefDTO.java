package com.epam.esm.bahlei.restbasic.controller.refdto;

import com.epam.esm.bahlei.restbasic.model.User;
import org.springframework.hateoas.RepresentationModel;

public class UserRefDTO extends RepresentationModel<UserRefDTO> {
  public long id;
  public String firstName;
  public String secondName;

  public UserRefDTO() {}

  public UserRefDTO(User user) {
    this.id = user.getId();
    this.firstName = user.getFirstName();
    this.secondName = user.getSecondName();
  }

  public long getId() {
    return id;
  }
}

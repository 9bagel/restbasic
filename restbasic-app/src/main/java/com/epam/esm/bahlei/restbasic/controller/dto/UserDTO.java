package com.epam.esm.bahlei.restbasic.controller.dto;

import com.epam.esm.bahlei.restbasic.model.User;
import org.springframework.hateoas.RepresentationModel;

public class UserDTO extends RepresentationModel<UserDTO> {
  public long id;
  public String username;
  public String firstName;
  public String lastName;
  public String email;
  public String password;

  public UserDTO() {}

  public UserDTO(User user) {
    this.id = user.getId();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.username = user.getUsername();
    this.email = user.getEmail();
  }
}

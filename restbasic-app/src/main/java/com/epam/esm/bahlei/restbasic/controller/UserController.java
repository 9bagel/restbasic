package com.epam.esm.bahlei.restbasic.controller;

import com.epam.esm.bahlei.restbasic.controller.dto.UserDTO;
import com.epam.esm.bahlei.restbasic.model.User;
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
@RequestMapping("/api")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/users")
  public ResponseEntity<?> addUser(
      @RequestBody UserDTO userDTO, HttpServletRequest httpServletRequest) {
    User user = toUser(userDTO);

    userService.save(user);

    return created(URI.create(httpServletRequest.getRequestURL().append(user.getId()).toString()))
        .build();
  }

  @GetMapping("/users")
  public ResponseEntity<?> getAll() {

    return ok(userService.getAll().stream().map(this::toUserDTO).collect(toList()));
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<?> getUser(@PathVariable long id) {
    Optional<User> optional = userService.get(id);
    if (!optional.isPresent()) {
      return status(HttpStatus.NOT_FOUND).build();
    }

    return ok(toUserDTO(optional.get()));
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
}

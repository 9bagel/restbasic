package com.epam.esm.bahlei.restbasic.controller;

import com.epam.esm.bahlei.restbasic.controller.dto.AuthenticationDTO;
import com.epam.esm.bahlei.restbasic.controller.dto.UserDTO;
import com.epam.esm.bahlei.restbasic.model.User;
import com.epam.esm.bahlei.restbasic.security.jwt.JwtTokenProvider;
import com.epam.esm.bahlei.restbasic.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;

  private final JwtTokenProvider jwtTokenProvider;

  private final UserService userService;

  @Autowired
  public AuthenticationController(
      AuthenticationManager authenticationManager,
      JwtTokenProvider jwtTokenProvider,
      UserService userService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.userService = userService;
  }

  @PreAuthorize("permitAll()")
  @PostMapping("/login")
  public ResponseEntity login(@RequestBody AuthenticationDTO requestDto) {
    try {
      String username = requestDto.getUsername();
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
      User user =
          userService
              .getByUsername(username)
              .orElseThrow(() -> new UsernameNotFoundException("user not found"));
      String token = jwtTokenProvider.createToken(user);

      Map<Object, Object> response = new HashMap<>();
      response.put("token", token);

      return ResponseEntity.ok(response);
    } catch (AuthenticationException e) {
      // Возвращать сразу Status Code
      throw new BadCredentialsException("Invalid username or password");
    }
  }

  @PreAuthorize("permitAll()")
  @PostMapping("/register")
  public ResponseEntity<?> register(
      @RequestBody UserDTO userDTO, HttpServletRequest httpServletRequest) {
    User user = toUser(userDTO);

    userService.register(user);

    return created(URI.create(httpServletRequest.getRequestURL().append(user.getId()).toString()))
        .build();
  }

  private User toUser(UserDTO userDTO) {
    User user = new User();
    user.setUsername(userDTO.getUsername());
    user.setPassword(userDTO.getPassword());
    user.setFirstName(userDTO.getFirstName());
    user.setLastName(userDTO.getLastName());
    user.setEmail(userDTO.getEmail());
    return user;
  }
}

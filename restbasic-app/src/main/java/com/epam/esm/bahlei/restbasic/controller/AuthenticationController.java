package com.epam.esm.bahlei.restbasic.controller;

import com.epam.esm.bahlei.restbasic.controller.dto.AuthenticationDTO;
import com.epam.esm.bahlei.restbasic.security.jwt.JwtTokenService;
import com.epam.esm.bahlei.restbasic.service.UserService;
import com.epam.esm.bahlei.restbasic.service.impl.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @Autowired
  public AuthenticationController(
      AuthenticationManager authenticationManager,
      JwtTokenService jwtTokenService,
      UserService userService,
      AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PreAuthorize("permitAll()")
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthenticationDTO requestDto) {
    Map<Object, Object> response = new HashMap<>();
    response.put(
        "token",
        authenticationService.getToken(requestDto.getUsername(), requestDto.getPassword()));

    return ResponseEntity.ok(response);
  }
}

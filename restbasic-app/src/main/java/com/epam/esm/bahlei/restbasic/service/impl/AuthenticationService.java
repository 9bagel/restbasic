package com.epam.esm.bahlei.restbasic.service.impl;

import com.epam.esm.bahlei.restbasic.model.User;
import com.epam.esm.bahlei.restbasic.security.jwt.JwtTokenService;
import com.epam.esm.bahlei.restbasic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

  private final JwtTokenService jwtTokenService;
  private final UserService userService;

  private final AuthenticationManager authenticationManager;

  @Autowired
  public AuthenticationService(
      JwtTokenService jwtTokenService,
      UserService userService,
      AuthenticationManager authenticationManager) {
    this.jwtTokenService = jwtTokenService;
    this.userService = userService;
    this.authenticationManager = authenticationManager;
  }

  public String getToken(String username, String password) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));
    } catch (AuthenticationException e) {
      throw new BadCredentialsException("Authentication failed");
    }
    User user =
        userService
            .getByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    return jwtTokenService.createToken(user);
  }
}

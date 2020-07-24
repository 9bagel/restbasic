package com.epam.esm.bahlei.restbasic.security;

import com.epam.esm.bahlei.restbasic.model.User;
import com.epam.esm.bahlei.restbasic.security.jwt.JwtUserFactory;
import com.epam.esm.bahlei.restbasic.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
  private final UserService userService;

  @Autowired
  public JwtUserDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        userService
            .getByUsername(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException("There is no user with username + " + username));

    return JwtUserFactory.create(user);
  }
}

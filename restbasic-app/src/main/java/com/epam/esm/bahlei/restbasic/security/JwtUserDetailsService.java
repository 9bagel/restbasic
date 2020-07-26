package com.epam.esm.bahlei.restbasic.security;

import com.epam.esm.bahlei.restbasic.model.Role;
import com.epam.esm.bahlei.restbasic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class JwtUserDetailsService implements UserDetailsService {
  private final UserService userService;

  @Autowired
  public JwtUserDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    com.epam.esm.bahlei.restbasic.model.User user =
        userService
            .getByUsername(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException("There is no user with username + " + username));

    return createSpringUser(user);
  }

  private User createSpringUser(com.epam.esm.bahlei.restbasic.model.User user) {

    return new User(
        user.getUsername(), user.getPassword(), mapToGrantedAuthorities(user.getRoles()));
  }

  private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> roles) {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(toList());
  }
}

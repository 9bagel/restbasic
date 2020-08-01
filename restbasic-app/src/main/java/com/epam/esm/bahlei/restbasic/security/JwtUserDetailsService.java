package com.epam.esm.bahlei.restbasic.security;

import com.epam.esm.bahlei.restbasic.model.Role;
import com.epam.esm.bahlei.restbasic.security.jwt.JwtUser;
import com.epam.esm.bahlei.restbasic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.singletonList;

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
            .orElseThrow(() -> new UsernameNotFoundException("Authentication failed"));

    return new JwtUser(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        mapToGrantedAuthorities(user.getRole()));
  }

  private static List<GrantedAuthority> mapToGrantedAuthorities(Role role) {
    return singletonList(new SimpleGrantedAuthority(role.name()));
  }
}

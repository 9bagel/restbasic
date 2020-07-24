package com.epam.esm.bahlei.restbasic.security.jwt;

import com.epam.esm.bahlei.restbasic.model.Role;
import com.epam.esm.bahlei.restbasic.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static java.util.stream.Collectors.toList;

public final class JwtUserFactory {
  public JwtUserFactory() {}

  public static JwtUser create(User user) {
    List<GrantedAuthority> grantedAuthorities = mapToGrantedAuthorities(user.getRoles());
    return new JwtUser(
        user.getId(),
        user.getUsername(),
        user.getFirstName(),
        user.getLastName(),
        user.getPassword(),
        user.getEmail(),
        user.getOrders(),
        grantedAuthorities);
  }

  private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> roles) {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(toList());
  }
}

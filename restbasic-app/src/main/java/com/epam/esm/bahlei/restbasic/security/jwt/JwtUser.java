package com.epam.esm.bahlei.restbasic.security.jwt;

import com.epam.esm.bahlei.restbasic.model.Order;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class JwtUser implements UserDetails {
  private final Long id;
  private final String username;
  private final String firstName;
  private final String lastName;
  private final String password;
  private final String email;
  private final Collection<? extends GrantedAuthority> authorities;
  private final List<Order> orders;

  public JwtUser(
      Long id,
      String username,
      String firstName,
      String lastName,
      String password,
      String email,
      List<Order> orders,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.password = password;
    this.email = email;
    this.orders = orders;
    this.authorities = authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @JsonIgnore
  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isEnabled() {
    return true;
  }

}

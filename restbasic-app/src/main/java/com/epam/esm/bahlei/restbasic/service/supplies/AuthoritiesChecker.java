package com.epam.esm.bahlei.restbasic.service.supplies;

import com.epam.esm.bahlei.restbasic.security.jwt.JwtUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.epam.esm.bahlei.restbasic.model.Role.ADMIN;

@Component
public class AuthoritiesChecker {
  public void check(long userId) {
    JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (!user.getAuthorities().contains(new SimpleGrantedAuthority(ADMIN.toString()))
        && user.getId() != userId) {
      throw new AccessDeniedException("Access denied");
    }
  }
}

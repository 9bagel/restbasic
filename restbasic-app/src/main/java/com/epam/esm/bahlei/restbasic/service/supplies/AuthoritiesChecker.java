package com.epam.esm.bahlei.restbasic.service.supplies;

import com.epam.esm.bahlei.restbasic.security.jwt.JwtUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthoritiesChecker {
  public void check(long userId) {
    JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (!user.isAdmin() && user.getId() != userId) {
      throw new AccessDeniedException("Access denied. userId = " + userId);
    }
  }
}

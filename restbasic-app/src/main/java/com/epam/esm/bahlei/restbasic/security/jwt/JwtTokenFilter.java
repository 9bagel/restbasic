package com.epam.esm.bahlei.restbasic.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

public class JwtTokenFilter extends GenericFilterBean {

  private final JwtTokenProvider jwtTokenProvider;

  @Autowired
  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
      throws IOException, ServletException {

    Optional<String> token = getTokenFromRequest((HttpServletRequest) req);
    if (token.isPresent() && jwtTokenProvider.validateToken(token.get())) {
      Authentication auth = jwtTokenProvider.getAuthentication(token.get());

      if (auth != null) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
      }
    }
    filterChain.doFilter(req, res);
  }

  private Optional<String> getTokenFromRequest(HttpServletRequest req) {
    Optional<String> bearerToken = Optional.ofNullable(req.getHeader("Authorization"));

    if (bearerToken.isPresent() && bearerToken.get().startsWith("Bearer ")) {
      return Optional.of(bearerToken.get().substring(7));
    }
    return Optional.empty();
  }
}

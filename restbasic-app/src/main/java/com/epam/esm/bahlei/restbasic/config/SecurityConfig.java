package com.epam.esm.bahlei.restbasic.config;

import com.epam.esm.bahlei.restbasic.handler.ExceptionHandlerFilter;
import com.epam.esm.bahlei.restbasic.model.Role;
import com.epam.esm.bahlei.restbasic.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import static com.epam.esm.bahlei.restbasic.model.Role.*;
import static com.epam.esm.bahlei.restbasic.model.Role.ADMIN;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final ExceptionHandlerFilter exceptionHandlerFilter;
  private final JwtTokenFilter jwtTokenFilter;

  @Autowired
  public SecurityConfig(
      ExceptionHandlerFilter exceptionHandlerFilter, JwtTokenFilter jwtTokenFilter) {
    this.exceptionHandlerFilter = exceptionHandlerFilter;
    this.jwtTokenFilter = jwtTokenFilter;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
    hierarchy.setHierarchy(ADMIN + " > " + USER);
    return hierarchy;
  }

  private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
    DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler =
        new DefaultWebSecurityExpressionHandler();
    defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
    return defaultWebSecurityExpressionHandler;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic()
        .disable()
        .csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(exceptionHandlerFilter, SecurityContextPersistenceFilter.class)
        .authorizeRequests()
        .expressionHandler(webExpressionHandler())
        .antMatchers("/api/**")
        .permitAll()
        .anyRequest()
        .authenticated();
  }
}

package com.epam.esm.bahlei.restbasic.service.authentication;

import com.epam.esm.bahlei.restbasic.model.User;
import com.epam.esm.bahlei.restbasic.security.jwt.JwtTokenService;
import com.epam.esm.bahlei.restbasic.service.UserService;
import com.epam.esm.bahlei.restbasic.service.impl.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {
  @Mock private JwtTokenService jwtTokenService;
  @Mock private UserService userService;
  @Mock private AuthenticationManager authenticationManager;
  @InjectMocks private AuthenticationService authenticationService;

  @Test
  void getToken_NonExistingUser_Error() {
    when(userService.getByUsername("username")).thenReturn(Optional.empty());

    assertThrows(
        UsernameNotFoundException.class,
        () -> authenticationService.getToken("username", "password"));
  }

  @Test
  void getToken_ExistingUser_OK() {
    when(userService.getByUsername("username")).thenReturn(Optional.of(new User()));

    assertThatCode(() -> authenticationService.getToken("username", "password"))
        .doesNotThrowAnyException();
  }
}

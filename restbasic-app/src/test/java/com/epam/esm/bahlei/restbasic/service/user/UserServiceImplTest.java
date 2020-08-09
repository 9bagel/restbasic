package com.epam.esm.bahlei.restbasic.service.user;

import com.epam.esm.bahlei.restbasic.dao.UserDAO;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Role;
import com.epam.esm.bahlei.restbasic.model.User;
import com.epam.esm.bahlei.restbasic.service.impl.UserServiceImpl;
import com.epam.esm.bahlei.restbasic.service.supplies.AuthoritiesChecker;
import com.epam.esm.bahlei.restbasic.service.validator.UserValidator;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
  @Mock private UserValidator validator;
  @Mock private UserDAO userDAO;
  @Mock private AuthoritiesChecker authoritiesChecker;
  @Mock private BCryptPasswordEncoder encoder;
  @InjectMocks private UserServiceImpl userService;

  @Test
  void get_ValidUserId_OK() {
    when(userDAO.get(1)).thenReturn(Optional.of(new User()));

    assertThatCode(() -> userService.get(1)).doesNotThrowAnyException();
  }

  @Test
  void get_InvalidUserId_Error() {
    assertThrows(ValidationException.class, () -> userService.get(-1));
  }

  @Test
  void register_ValidUser_OK() {
    User validUser = getValidUser();

    assertThatCode(() -> userService.register(validUser)).doesNotThrowAnyException();
  }

  @Test
  void register_InvalidUser_Error() {
    User user = getValidUser();
    user.setEmail("invalid email");
    when(validator.validate(user)).thenReturn(singletonList("error"));

    assertThrows(ValidationException.class, () -> userService.register(user));
  }

  @Test
  void getByUsername_ExistingUsername_NotEmpty() {
    when(userDAO.getByUsername("user")).thenReturn(Optional.of(new User()));

    Optional<User> actual = userService.getByUsername("user");

    assertThat(actual).isNotEmpty();
  }

  @Test
  void getByUsername_NonExistingUsername_Empty() {
    when(userDAO.getByUsername("noneExistingName")).thenReturn(Optional.empty());

    Optional<User> actual = userService.getByUsername("noneExistingName");

    assertThat(actual).isEmpty();
  }

  @Test
  void getAll_OK() {
    Pageable pageable = new Pageable(1, 10);
    List<User> expected = singletonList(getValidUser());
    when(userDAO.getAll(pageable)).thenReturn(expected);

    List<User> actual = userService.getAll(pageable);

    assertThat(actual).isEqualTo(expected);
  }

  private User getValidUser() {
    User user = new User();
    user.setEmail("test@epam.com");
    user.setFirstName("Test");
    user.setLastName("Test");
    user.setPassword("test");
    user.setRole(Role.USER);
    user.setId(1L);
    user.setOrders(ImmutableList.of());

    return user;
  }
}

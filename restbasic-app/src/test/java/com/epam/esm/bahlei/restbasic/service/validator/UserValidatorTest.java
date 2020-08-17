package com.epam.esm.bahlei.restbasic.service.validator;

import com.epam.esm.bahlei.restbasic.dao.UserDAO;
import com.epam.esm.bahlei.restbasic.model.Role;
import com.epam.esm.bahlei.restbasic.model.User;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

  @InjectMocks private UserValidator userValidator;
  @Mock private UserDAO userDAO;

  @Test
  void validateEmail_ValidEmail_OK() {
    User userWithValidEmail = getValidUser();
    userWithValidEmail.setEmail("username@epam.com");

    List<String> actual = userValidator.validate(userWithValidEmail);

    assertThat(actual).isEmpty();
  }

  @Test
  void validateEmail_InvalidEmail_Error() {
    User user = getValidUser();
    user.setEmail("         ");

    List<String> actual = userValidator.validate(user);

    assertThat(actual).containsOnly("Invalid Email!");
  }

  @Test
  void validateEmail_Null_Error() {
    User user = getValidUser();
    user.setEmail(null);

    List<String> actual = userValidator.validate(user);

    assertThat(actual).containsOnly("email should not be empty!");
  }

  @Test
  void validateEmail_EmailAlreadyExists_Error() {
    User user = getValidUser();
    user.setEmail("existingEmail@epam.com");
    when(userDAO.getByEmail("existingEmail@epam.com")).thenReturn(Optional.of(new User()));

    List<String> actual = userValidator.validate(user);

    assertThat(actual).containsOnly("Email is already taken");
  }

  @Test
  void validateUsername_ValidUserName_OK() {
    User user = getValidUser();
    user.setUsername("user");

    List<String> actual = userValidator.validate(user);

    assertThat(actual).isEmpty();
  }

  @Test
  void validateUsername_Null_Error() {
    User user = getValidUser();
    user.setUsername(null);

    List<String> actual = userValidator.validate(user);

    assertThat(actual).containsOnly("Username should not be empty!");
  }

  @Test
  void validateUsername_UsernameAlreadyExists_Error() {
    User user = getValidUser();
    user.setUsername("existingUsername");
    when(userDAO.getByUsername("existingUsername")).thenReturn(Optional.of(new User()));

    List<String> actual = userValidator.validate(user);

    assertThat(actual).containsOnly("Username is already taken");
  }

  @Test
  void validateFirstName_Null_Error() {
    User user = getValidUser();
    user.setFirstName(null);

    List<String> actual = userValidator.validate(user);

    assertThat(actual).containsOnly("FirstName should not be empty!");
  }

  @Test
  void validateLastName_Null_Error() {
    User user = getValidUser();
    user.setLastName(null);

    List<String> actual = userValidator.validate(user);

    assertThat(actual).containsOnly("LastName should not be empty!");
  }

  private User getValidUser() {
    User user = new User();
    user.setUsername("userName");
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

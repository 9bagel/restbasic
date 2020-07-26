package com.epam.esm.bahlei.restbasic.service.validator;

import com.epam.esm.bahlei.restbasic.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.apache.commons.validator.routines.EmailValidator.getInstance;

@Component
public class UserValidator {

  public UserValidator() {}

  public List<String> validate(User user) {
    List<String> errors = new ArrayList<>();
    validateUsername(user.getUsername(), errors);
    validateEmail(user.getEmail(), errors);
    validateFirstName(user.getFirstName(), errors);
    validateLastName(user.getLastName(), errors);
    return errors;
  }

  private void validateEmail(String email, List<String> errors) {
    if (!getInstance().isValid(email)) {
      errors.add("Invalid Email!");
    }
  }

  private void validateUsername(String username, List<String> errors) {
    if (isNullOrEmpty(username)) {
      errors.add("Username should not be empty!");
    }
  }

  private void validateFirstName(String firstName, List<String> errors) {
    if (isNullOrEmpty(firstName)) {
      errors.add("FirstName should not be empty!");
    }
  }

  private void validateLastName(String lastName, List<String> errors) {
    if (isNullOrEmpty(lastName)) {
      errors.add("LastName should not be empty!");
    }
  }
}

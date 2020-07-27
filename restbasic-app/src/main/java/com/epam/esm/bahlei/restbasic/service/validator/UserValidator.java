package com.epam.esm.bahlei.restbasic.service.validator;

import com.epam.esm.bahlei.restbasic.model.User;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

@Component
public class UserValidator {

  private EmailValidator emailValidator;

  public UserValidator(EmailValidator emailValidator) {
    this.emailValidator = emailValidator;
  }

  public List<String> validate(User user) {
    List<String> errors = new ArrayList<>();

    validateUsername(user.getUsername(), errors);
    validateEmail(user.getEmail(), errors);
    validateFirstName(user.getFirstName(), errors);
    validateLastName(user.getLastName(), errors);
    return errors;
  }
  // Проверку на unique
  private void validateEmail(String email, List<String> errors) {
    if (!emailValidator.isValid(email)) {
      errors.add("Invalid Email!");
    }
  }
  // Проверку на unique
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

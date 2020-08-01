package com.epam.esm.bahlei.restbasic.service.validator;

import com.epam.esm.bahlei.restbasic.dao.UserDAO;
import com.epam.esm.bahlei.restbasic.model.User;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.apache.commons.validator.routines.EmailValidator.getInstance;

@Component
public class UserValidator {
  private final UserDAO userDAO;

  @Autowired
  public UserValidator(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  public void validate(User user) {
    List<String> errors = new ArrayList<>();

    validateUsername(user.getUsername(), errors);
    validateEmail(user.getEmail(), errors);
    validateFirstName(user.getFirstName(), errors);
    validateLastName(user.getLastName(), errors);

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
  }

  private void validateEmail(String email, List<String> errors) {
    if (email == null) {
      errors.add("email should not be empty!");
      return;
    }

    EmailValidator emailValidator = getInstance();
    if (!emailValidator.isValid(email)) {
      errors.add("Invalid Email!");
    }

    userDAO.getByEmail(email).ifPresent(user -> errors.add("Email is already taken"));
  }

  private void validateUsername(String username, List<String> errors) {

    if (isNullOrEmpty(username)) {
      errors.add("Username should not be empty!");
    }
    userDAO.getByUsername(username).ifPresent(user -> errors.add("Username is already taken"));
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

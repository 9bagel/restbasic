package com.epam.esm.bahlei.restbasic.service.validator;

import com.epam.esm.bahlei.restbasic.dao.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserValidator {
  private final UserDAO userDAO;

  @Autowired
  public UserValidator(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  public List<String> validate(long userId) {
    List<String> errors = new ArrayList<>();
    validateUserId(userId, errors);
    return errors;
  }

  private void validateUserId(long userId, List<String> errors) {
    if (userId <= 0) {
      errors.add("user id should be > 0");
      return;
    }
    if (!userDAO.get(userId).isPresent()) {
      errors.add("There is no user with id " + userId);
    }
  }
}

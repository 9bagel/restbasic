package com.epam.esm.bahlei.restbasic.service.impl;

import com.epam.esm.bahlei.restbasic.dao.UserDAO;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Role;
import com.epam.esm.bahlei.restbasic.model.User;
import com.epam.esm.bahlei.restbasic.service.UserService;
import com.epam.esm.bahlei.restbasic.service.supplies.AuthoritiesChecker;
import com.epam.esm.bahlei.restbasic.service.validator.UserValidator;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  private final UserDAO userDAO;
  private final BCryptPasswordEncoder encoder;
  private final UserValidator validator;
  private final AuthoritiesChecker authoritiesChecker;

  @Autowired
  public UserServiceImpl(
      UserDAO userDAO,
      BCryptPasswordEncoder encoder,
      UserValidator validator,
      AuthoritiesChecker authoritiesChecker) {
    this.userDAO = userDAO;
    this.encoder = encoder;
    this.validator = validator;
    this.authoritiesChecker = authoritiesChecker;
  }

  @Override
  public List<User> getAll(Pageable pageable) {
    return userDAO.getAll(pageable);
  }

  @Override
  public Optional<User> get(long userId) {
    if (userId <= 0) {
      throw new ValidationException(ImmutableList.of("User id cannot be negative or zero"));
    }

    authoritiesChecker.check(userId);

    return userDAO.get(userId);
  }

  @Transactional
  @Override
  public void register(User user) {
    List<String> errors = validator.validate(user);

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    user.setPassword(encoder.encode(user.getPassword()));
    user.setRole(Role.USER);

    userDAO.save(user);
  }

  @Transactional
  @Override
  public Optional<User> getByUsername(String username) {
    return userDAO.getByUsername(username);
  }
}

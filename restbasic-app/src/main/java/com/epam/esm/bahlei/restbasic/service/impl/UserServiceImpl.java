package com.epam.esm.bahlei.restbasic.service.impl;

import com.epam.esm.bahlei.restbasic.dao.UserDAO;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Role;
import com.epam.esm.bahlei.restbasic.model.User;
import com.epam.esm.bahlei.restbasic.service.UserService;
import com.epam.esm.bahlei.restbasic.service.validator.UserValidator;
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

  @Autowired
  public UserServiceImpl(UserDAO userDAO, BCryptPasswordEncoder encoder) {
    this.userDAO = userDAO;
    this.encoder = encoder;
  }

  @Override
  public List<User> getAll(Pageable pageable) {
    return userDAO.getAll(pageable);
  }

  @Override
  public Optional<User> get(long id) {
    return userDAO.get(id);
  }

  @Transactional
  @Override
  public void register(User user) {
    UserValidator validator = new UserValidator();
    validator.validate(user);

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

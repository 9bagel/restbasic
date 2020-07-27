package com.epam.esm.bahlei.restbasic.service.impl;

import com.epam.esm.bahlei.restbasic.dao.RoleDAO;
import com.epam.esm.bahlei.restbasic.dao.UserDAO;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Role;
import com.epam.esm.bahlei.restbasic.model.User;
import com.epam.esm.bahlei.restbasic.service.UserService;
import com.epam.esm.bahlei.restbasic.service.validator.UserValidator;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import com.google.common.collect.ImmutableList;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  private final UserDAO userDAO;
  private final RoleDAO roleDAO;
  private final BCryptPasswordEncoder encoder;

  @Autowired
  public UserServiceImpl(UserDAO userDAO, RoleDAO roleDAO, BCryptPasswordEncoder encoder) {
    this.userDAO = userDAO;
    this.roleDAO = roleDAO;
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
    UserValidator validator = new UserValidator(EmailValidator.getInstance());
    validator.validate(user);

    Role userRole =
        roleDAO
            .getByName("role_user")
            .orElseThrow(() -> new ValidationException(ImmutableList.of("there is no role USER")));
    List<Role> roles = new ArrayList<>();
    roles.add(userRole);

    user.setPassword(encoder.encode(user.getPassword()));
    user.setRoles(roles);

    userDAO.save(user);
  }

  @Transactional
  @Override
  public Optional<User> getByUsername(String username) {
    return userDAO.getByUsername(username);
  }
}

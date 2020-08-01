package com.epam.esm.bahlei.restbasic.service.impl;

import com.epam.esm.bahlei.restbasic.dao.UserDAO;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Role;
import com.epam.esm.bahlei.restbasic.model.User;
import com.epam.esm.bahlei.restbasic.security.jwt.JwtUser;
import com.epam.esm.bahlei.restbasic.service.UserService;
import com.epam.esm.bahlei.restbasic.service.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
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

  @Autowired
  public UserServiceImpl(UserDAO userDAO, BCryptPasswordEncoder encoder, UserValidator validator) {
    this.userDAO = userDAO;
    this.encoder = encoder;
    this.validator = validator;
  }

  @Override
  public List<User> getAll(Pageable pageable) {
    return userDAO.getAll(pageable);
  }

  @Override
  public Optional<User> get(long id) {
    JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (user.getId() != id) {
      throw new AccessDeniedException("Access denied");
    }
    return userDAO.get(id);
  }

  @Transactional
  @Override
  public void register(User user) {
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

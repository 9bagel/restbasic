package com.epam.esm.bahlei.restbasic.service;

import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
  List<User> getAll(Pageable pageable);

  Optional<User> get(long id);

  void register(User user);

  Optional<User> getByUsername(String username);
}

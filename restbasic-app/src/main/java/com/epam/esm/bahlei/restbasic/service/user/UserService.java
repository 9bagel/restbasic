package com.epam.esm.bahlei.restbasic.service.user;

import com.epam.esm.bahlei.restbasic.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
  List<User> getAll(int page, int size);

  void save(User user);

  Optional<User> get(long id);
}

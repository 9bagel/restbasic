package com.epam.esm.bahlei.restbasic.dao.user;

import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
  List<User> getAll(Pageable pageable);

  void save(User user);

  Optional<User> get(long id);
}

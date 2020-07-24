package com.epam.esm.bahlei.restbasic.dao.role;

import com.epam.esm.bahlei.restbasic.model.Role;

import java.util.Optional;

public interface RoleDAO {
  Optional<Role> getByName(String name);
}

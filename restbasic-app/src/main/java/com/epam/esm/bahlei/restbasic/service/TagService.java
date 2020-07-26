package com.epam.esm.bahlei.restbasic.service;

import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {

  Optional<Tag> get(long tagId);

  List<Tag> getAll(Pageable pageable);

  void save(Tag tag);

  void delete(long tagId);
}

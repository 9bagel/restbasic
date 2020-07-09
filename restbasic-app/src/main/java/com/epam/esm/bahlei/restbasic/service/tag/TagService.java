package com.epam.esm.bahlei.restbasic.service.tag;

import com.epam.esm.bahlei.restbasic.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {

  Optional<Tag> get(long tagId);

  List<Tag> getAll(int page, int size);

  void save(Tag tag);

  void delete(long tagId);

  Optional<Tag> getTagByName(String tagName);
}

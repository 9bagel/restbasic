package com.epam.esm.bahlei.restbasic.service.impl;

import com.epam.esm.bahlei.restbasic.dao.TagDAO;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Tag;
import com.epam.esm.bahlei.restbasic.service.TagService;
import com.epam.esm.bahlei.restbasic.service.validator.TagValidator;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Service
public class TagServiceImpl implements TagService {

  private final TagDAO tagDAO;
  private final TagValidator tagValidator;

  @Autowired
  public TagServiceImpl(TagDAO tagDAO, TagValidator tagValidator) {
    this.tagDAO = tagDAO;
    this.tagValidator = tagValidator;
  }

  @Override
  public Optional<Tag> get(long tagId) {
    if (tagId <= 0) {
      throw new ValidationException(singletonList("Tag id can't be negative or zero"));
    }
    return tagDAO.get(tagId);
  }

  @Override
  public List<Tag> getAll(Pageable pageable) {
    return tagDAO.getAll(pageable);
  }

  @Override
  @Transactional
  public void save(Tag tag) {
    List<String> errors = tagValidator.validate(tag);

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    tagDAO.save(tag);
  }

  @Override
  @Transactional
  public void delete(long tagId) {
    if (tagId <= 0) {
      throw new ValidationException(singletonList("Tag id can't be negative or zero"));
    }
    tagDAO.delete(tagId);
  }
}

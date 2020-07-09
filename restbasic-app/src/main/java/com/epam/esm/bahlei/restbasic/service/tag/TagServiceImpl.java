package com.epam.esm.bahlei.restbasic.service.tag;

import com.epam.esm.bahlei.restbasic.dao.tag.TagDAO;
import com.epam.esm.bahlei.restbasic.model.Tag;
import com.epam.esm.bahlei.restbasic.service.validator.GlobalValidator;
import com.epam.esm.bahlei.restbasic.service.validator.TagValidator;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    return tagDAO.get(tagId);
  }

  @Override
  public List<Tag> getAll(int page, int size) {
    List<String> errors = GlobalValidator.validatePagination(page, size);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    int offset = size * (page - 1);
    return tagDAO.getAll(size, offset);
  }

  @Override
  public void save(Tag tag) {
    List<String> errors = tagValidator.validate(tag);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    tagDAO.save(tag);
  }

  @Override
  public void delete(long tagId) {
    tagDAO.delete(tagId);
  }

  @Override
  public Optional<Tag> getTagByName(String tagName) {
    return tagDAO.getByName(tagName);
  }
}

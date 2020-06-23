package com.epam.esm.bahlei.restbasic.service.validator;

import com.epam.esm.bahlei.restbasic.dao.tag.TagDAO;
import com.epam.esm.bahlei.restbasic.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagValidator {
  private final TagDAO tagDAO;

  @Autowired
  public TagValidator(TagDAO tagDAO) {
    this.tagDAO = tagDAO;
  }

  public List<String> validate(Tag tag) {
    List<String> errorMessages = new ArrayList<>();
    validateName(tag.getName(), errorMessages);

    return errorMessages;
  }

  private void validateName(String tagName, List<String> errorMessages) {
    if (tagName == null || tagName.trim().isEmpty()) {
      errorMessages.add("Tag name should not be empty.");
    }
    if (tagDAO.getByName(tagName).isPresent()) {
      errorMessages.add(String.format("Tag with name %s already exists.", tagName));
    }
  }
}

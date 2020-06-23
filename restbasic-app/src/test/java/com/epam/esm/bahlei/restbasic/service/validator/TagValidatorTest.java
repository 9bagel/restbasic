package com.epam.esm.bahlei.restbasic.service.validator;

import com.epam.esm.bahlei.restbasic.dao.tag.TagDAO;
import com.epam.esm.bahlei.restbasic.model.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagValidatorTest {
  @InjectMocks TagValidator tagValidator;
  @Mock TagDAO tagDAO;
  @Test
  void validate_NullName_Error() {
    Tag tag = getValidTag();
    tag.setName(null);

    List<String> errors = tagValidator.validate(tag);

    assertThat(errors).containsOnly("Tag name should not be empty.");
  }

  @Test
  void validate_EmptyName_Error() {
    Tag tag = getValidTag();
    tag.setName("   ");

    List<String> errors = tagValidator.validate(tag);

    assertThat(errors).containsOnly("Tag name should not be empty.");
  }

  @Test
  void validate_ValidName_EmptyList() {
    Tag tag = getValidTag();
    tag.setName("validName");
    when(tagDAO.getByName("validName")).thenReturn(Optional.empty());

    List<String> errors = tagValidator.validate(tag);

    assertThat(errors).isEmpty();
  }

  @Test
  void validate_AlreadyExistingName_Error() {
    Tag tag = getValidTag();
    tag.setName("test");
    when(tagDAO.getByName("test")).thenReturn(Optional.of(getValidTag()));

    List<String> errors = tagValidator.validate(tag);

    assertThat(errors).containsOnly("Tag with name test already exists.");
  }

  private Tag getValidTag() {
    Tag tag = new Tag();
    tag.setName("validName");
    tag.setId(1);

    return tag;
  }
}

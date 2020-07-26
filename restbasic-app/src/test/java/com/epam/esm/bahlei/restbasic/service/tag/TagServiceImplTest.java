package com.epam.esm.bahlei.restbasic.service.tag;

import com.epam.esm.bahlei.restbasic.dao.TagDAO;
import com.epam.esm.bahlei.restbasic.model.Tag;
import com.epam.esm.bahlei.restbasic.service.impl.TagServiceImpl;
import com.epam.esm.bahlei.restbasic.service.validator.TagValidator;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
  @Mock private TagValidator validator;
  @Mock private TagDAO tagDAO;
  @InjectMocks private TagServiceImpl tagService;

  @Test
  void save_ValidTag_OK() {
    Tag tag = getValidTag();
    when(validator.validate(tag)).thenReturn(emptyList());

    assertThatCode(() -> tagService.save(tag)).doesNotThrowAnyException();
  }

  @Test
  void save_InValidTag_OK() {
    Tag tag = getValidTag();
    tag.setName("invalidName");
    when(validator.validate(tag)).thenReturn(singletonList("error"));

    assertThrows(ValidationException.class, () -> tagService.save(tag));
  }

  private Tag getValidTag() {
    return new Tag(1, "books");
  }
}

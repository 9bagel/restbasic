package com.epam.esm.bahlei.restbasic.service.tag;

import com.epam.esm.bahlei.restbasic.dao.TagDAO;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Tag;
import com.epam.esm.bahlei.restbasic.service.impl.TagServiceImpl;
import com.epam.esm.bahlei.restbasic.service.validator.TagValidator;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
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
  void save_InValidTag_Error() {
    Tag tag = getValidTag();
    tag.setName("invalidName");
    when(validator.validate(tag)).thenReturn(singletonList("error"));

    assertThrows(ValidationException.class, () -> tagService.save(tag));
  }

  @Test
  void get_ExistingTagId_OK() {
    Optional<Tag> expected = Optional.of(getValidTag());
    when(tagDAO.get(1)).thenReturn(expected);

    Optional<Tag> actual = tagService.get(1);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void get_NonExistingTagId_Empty() {
    when(tagDAO.get(1)).thenReturn(Optional.empty());

    Optional<Tag> actual = tagService.get(1);

    assertThat(actual).isEmpty();
  }

  @Test
  void get_NotValidTagId_Error() {
    assertThrows(ValidationException.class, () -> tagService.get(-1));
  }

  @Test
  void getAll_OK() {
    Pageable pageable = new Pageable(1, 10);
    List<Tag> expected = singletonList(getValidTag());
    when(tagDAO.getAll(pageable)).thenReturn(expected);

    List<Tag> actual = tagService.getAll(pageable);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void delete_ExistingTagID_OK() {
    assertThatCode(() -> tagService.delete(1)).doesNotThrowAnyException();
  }

  @Test
  void delete_NotValidTagID_Error() {
    assertThrows(ValidationException.class, () -> tagService.delete(-1));
  }

  private Tag getValidTag() {
    return new Tag(1, "books");
  }
}

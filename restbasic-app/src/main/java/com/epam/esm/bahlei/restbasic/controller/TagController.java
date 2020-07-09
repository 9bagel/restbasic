package com.epam.esm.bahlei.restbasic.controller;

import com.epam.esm.bahlei.restbasic.config.exception.response.ErrorResponse;
import com.epam.esm.bahlei.restbasic.controller.dto.TagDTO;
import com.epam.esm.bahlei.restbasic.model.Tag;
import com.epam.esm.bahlei.restbasic.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/api")
public class TagController {
  private final TagService tagService;

  @Autowired
  public TagController(TagService tagService) {
    this.tagService = tagService;
  }
  /**
   * Returns a single Tag instance for a given id. See {@link #getAll(int, int)} to return a list of tags
   * and determine individual {@code id} Path [GET /api/certificates/{id}] Path [GET /api/tags/{id}]
   *
   * @param id an id of a tag
   */
  @GetMapping("/tags/{id}")
  public ResponseEntity<?> getTag(@PathVariable long id) {
    Optional<Tag> optional = tagService.get(id);
    if (!optional.isPresent()) {
      return status(HttpStatus.NOT_FOUND).build();
    }

    return ok(toTagDTO(optional.get()));
  }

  /** Returns a list of Tags Path [GET /api/tags/] */
  @GetMapping("/tags")
  public ResponseEntity<?> getAll(
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    return ok(tagService.getAll(page, size).stream().map(this::toTagDTO).collect(toList()));
  }

  /**
   * Create a tag Path [POST /api/tags/]
   *
   * @param tagDTO a Tag object in JSON formaat
   */
  @PostMapping("/tags")
  public ResponseEntity<?> addTag(
      @RequestBody TagDTO tagDTO, HttpServletRequest httpServletRequest) {
    Tag tag = toTag(tagDTO);

    tagService.save(tag);

    return created(URI.create(httpServletRequest.getRequestURL().append(tag.getId()).toString()))
        .build();
  }
  /**
   * Deletes a single tag for a specific id. Path [DELETE /api/tags/{id}]
   *
   * @param id tag id
   */
  @DeleteMapping("/tags/{id}")
  public ResponseEntity<ErrorResponse> deleteTag(@PathVariable long id) {
    if (!tagService.get(id).isPresent()) {
      return notFound().build();
    }
    tagService.delete(id);
    return noContent().build();
  }

  private TagDTO toTagDTO(Tag tag) {

    return new TagDTO(tag);
  }

  private Tag toTag(TagDTO tagDTO) {
    Tag tag = new Tag();
    tag.setId(tagDTO.id);
    tag.setName(tagDTO.name);

    return tag;
  }
}

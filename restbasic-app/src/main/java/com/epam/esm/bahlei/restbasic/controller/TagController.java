package com.epam.esm.bahlei.restbasic.controller;

import com.epam.esm.bahlei.restbasic.controller.dto.TagDTO;
import com.epam.esm.bahlei.restbasic.controller.dto.response.ErrorResponse;
import com.epam.esm.bahlei.restbasic.controller.linkmapper.LinkMapper;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Tag;
import com.epam.esm.bahlei.restbasic.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/api")
public class TagController {
  private final TagService tagService;
  private final LinkMapper linkMapper;

  @Autowired
  public TagController(TagService tagService, LinkMapper linkMapper) {
    this.tagService = tagService;
    this.linkMapper = linkMapper;
  }
  /**
   * Returns a single Tag instance for a given id. See {@link #getAll(int, int)} to return a list of
   * tags and determine individual {@code id} Path [GET /api/certificates/{id}] Path [GET
   * /api/tags/{id}]
   *
   * @param tagId an id of a tag
   */
  @PreAuthorize("hasAuthority('USER')")
  @GetMapping("/tags/{tagId}")
  public ResponseEntity<?> getTag(@PathVariable long tagId) {
    Optional<Tag> optional = tagService.get(tagId);
    if (!optional.isPresent()) {
      return status(NOT_FOUND).build();
    }
    TagDTO tagDTO = new TagDTO(optional.get());
    linkMapper.mapLinks(tagDTO);
    return ok(tagDTO);
  }

  /** Returns a list of Tags Path [GET /api/tags/] */
  @PreAuthorize("hasAuthority('USER')")
  @GetMapping("/tags")
  public ResponseEntity<?> getAll(
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    return ok(
        tagService.getAll(new Pageable(page, size)).stream()
            .map(TagDTO::new)
            .peek(linkMapper::mapLinks)
            .collect(toList()));
  }

  /**
   * Create a tag Path [POST /api/tags/]
   *
   * @param tagDTO a Tag object in JSON format
   */
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("/tags")
  public ResponseEntity<?> addTag(
      @RequestBody TagDTO tagDTO) {
    Tag tag = new Tag(tagDTO.id, tagDTO.name);

    tagService.save(tag);

    return created(linkTo(methodOn(TagController.class).getTag(tag.getId())).toUri()).build();
  }
  /**
   * Deletes a single tag for a specific id. Path [DELETE /api/tags/{id}]
   *
   * @param id tag id
   */
  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping("/tags/{id}")
  public ResponseEntity<ErrorResponse> deleteTag(@PathVariable long id) {
    if (!tagService.get(id).isPresent()) {
      return notFound().build();
    }
    tagService.delete(id);
    return noContent().build();
  }


}

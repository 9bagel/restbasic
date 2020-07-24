package com.epam.esm.bahlei.restbasic.controller;

import com.epam.esm.bahlei.restbasic.config.exception.response.ErrorResponse;
import com.epam.esm.bahlei.restbasic.controller.dto.TagDTO;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Tag;
import com.epam.esm.bahlei.restbasic.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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
   * Returns a single Tag instance for a given id. See {@link #getAll(int, int)} to return a list of
   * tags and determine individual {@code id} Path [GET /api/certificates/{id}] Path [GET
   * /api/tags/{id}]
   *
   * @param id an id of a tag
   */
  @PreAuthorize("hasAuthority('role_user')")
  @GetMapping("/tags/{id}")
  public ResponseEntity<?> getTag(@PathVariable long id) {
    Optional<Tag> optional = tagService.get(id);
    if (!optional.isPresent()) {
      return status(HttpStatus.NOT_FOUND).build();
    }
    TagDTO tagDTO = toTagDTO(optional.get());
    tagDTO.add(linkTo(methodOn(TagController.class).getTag(id)).withSelfRel());
    return ok(tagDTO);
  }

  /** Returns a list of Tags Path [GET /api/tags/] */
  @PreAuthorize("hasAuthority('role_user')")
  @GetMapping("/tags")
  public ResponseEntity<?> getAll(
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    return ok(
        tagService.getAll(new Pageable(page, size)).stream()
            .map(this::toTagDTO)
            .map(
                tagDTO ->
                    tagDTO.add(
                        linkTo(methodOn(TagController.class).getTag(tagDTO.id)).withSelfRel()))
            .collect(toList()));
  }

  /**
   * Create a tag Path [POST /api/tags/]
   *
   * @param tagDTO a Tag object in JSON format
   */
  @PreAuthorize("hasAuthority('role_admin')")
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
  @PreAuthorize("hasAuthority('role_admin')")
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

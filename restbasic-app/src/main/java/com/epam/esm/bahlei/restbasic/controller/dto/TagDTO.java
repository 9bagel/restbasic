package com.epam.esm.bahlei.restbasic.controller.dto;

import com.epam.esm.bahlei.restbasic.model.Tag;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

@Component
public class TagDTO extends RepresentationModel<TagDTO> {
  public long id;
  public String name;

  public TagDTO(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public TagDTO(Tag tag) {
    this.id = tag.getId();
    this.name = tag.getName();
  }

  public TagDTO() {
  }
}

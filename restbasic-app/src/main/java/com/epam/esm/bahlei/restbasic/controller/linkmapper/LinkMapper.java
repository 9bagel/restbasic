package com.epam.esm.bahlei.restbasic.controller.linkmapper;

import com.epam.esm.bahlei.restbasic.controller.CertificateController;
import com.epam.esm.bahlei.restbasic.controller.TagController;
import com.epam.esm.bahlei.restbasic.controller.dto.GiftCertificateDTO;
import com.epam.esm.bahlei.restbasic.controller.dto.TagDTO;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LinkMapper {

  public void mapLinks(TagDTO dto) {
    dto.add(linkTo(methodOn(TagController.class).getTag(dto.id)).withSelfRel());
  }

  public void mapLinks(List<TagDTO> tags) {
    tags.forEach(
        tagDTO ->
            tagDTO.add(linkTo(methodOn(TagController.class).getTag(tagDTO.id)).withSelfRel()));
  }

  public void mapLinks(GiftCertificateDTO certificateDTO) {
    certificateDTO.add(
        linkTo(methodOn(CertificateController.class).getCertificate(certificateDTO.id))
            .withSelfRel());

    if (!certificateDTO.tags.isEmpty()) {
      mapLinks(certificateDTO.tags);
    }
  }
}

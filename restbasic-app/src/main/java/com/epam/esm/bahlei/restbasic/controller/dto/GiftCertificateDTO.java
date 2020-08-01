package com.epam.esm.bahlei.restbasic.controller.dto;

import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class GiftCertificateDTO extends RepresentationModel<GiftCertificateDTO> {
  public long id;
  public String name;
  public String description;
  public BigDecimal price;
  public Integer duration;

  @JsonFormat public Instant createdAt;

  @JsonFormat public Instant modifiedAt;

  public List<TagDTO> tags;

  public GiftCertificateDTO(GiftCertificate certificate) {
    this.id = certificate.getId();
    this.createdAt = certificate.getAudit().getCreatedAt();
    this.description = certificate.getDescription();
    this.duration = certificate.getDuration();
    this.modifiedAt = certificate.getAudit().getUpdatedAt();
    this.name = certificate.getName();
    this.price = certificate.getPrice();
    this.tags = certificate.getTags().stream().map(TagDTO::new).collect(toList());
  }

  public GiftCertificateDTO() {}
}

package com.epam.esm.bahlei.restbasic.controller.dto;

import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class GiftCertificateDTO {
  public long id;
  public String name;
  public String description;
  public BigDecimal price;
  public int duration;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  public LocalDateTime createdAt;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  public LocalDateTime modifiedAt;

  public List<Tag> tags;

  public GiftCertificateDTO(GiftCertificate certificate) {
    this.id = certificate.getId();
    this.createdAt = certificate.getCreatedAt();
    this.description = certificate.getDescription();
    this.duration = certificate.getDuration();
    this.modifiedAt = certificate.getModifiedAt();
    this.name = certificate.getName();
    this.price = certificate.getPrice();
    this.tags = certificate.getTags();
  }

  public GiftCertificateDTO() {}
}

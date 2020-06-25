package com.epam.esm.bahlei.restbasic.model;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class GiftCertificate {
  private long id;
  private String name;
  private String description;
  private BigDecimal price;
  private List<Tag> tags;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private Integer duration;

  public GiftCertificate() {}

  public GiftCertificate(
      long id,
      String name,
      String description,
      BigDecimal price,
      LocalDateTime createdAt,
      LocalDateTime modifiedAt,
      int duration) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
    this.duration = duration;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getModifiedAt() {
    return modifiedAt;
  }

  public void setModifiedAt(LocalDateTime modifiedAt) {
    this.modifiedAt = modifiedAt;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }
}

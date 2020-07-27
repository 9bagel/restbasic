package com.epam.esm.bahlei.restbasic.model;

import com.epam.esm.bahlei.restbasic.dao.audit.AuditListener;
import com.epam.esm.bahlei.restbasic.model.audit.Audit;
import com.epam.esm.bahlei.restbasic.model.audit.Auditable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "certificates")
@EntityListeners(AuditListener.class)
public class GiftCertificate implements Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certificate_generator")
  @SequenceGenerator(
      name = "certificate_generator",
      sequenceName = "certificates_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "name")
  private String name;

  @Embedded private Audit audit;

  @Column(name = "description")
  private String description;

  @Column(name = "price")
  private BigDecimal price;

  @ManyToMany(cascade=CascadeType.ALL)
  @JoinTable(
      name = "certificate_tags",
      joinColumns = @JoinColumn(name = "certificate_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private List<Tag> tags;

  @Column(name = "duration")
  private Integer duration;

  public GiftCertificate() {}

  public GiftCertificate(
      long id, String name, String description, BigDecimal price, Integer duration) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.duration = duration;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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

  @Override
  public Audit getAudit() {
    return audit;
  }

  @Override
  public void setAudit(Audit audit) {
    this.audit = audit;
  }
}

package com.epam.esm.bahlei.restbasic.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
  @SequenceGenerator(name = "order_generator", sequenceName = "orders_id_seq", allocationSize = 1)
  private long id;

  @ManyToOne
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private User user;

  @Column(name = "user_id")
  private long userId;

  @JoinColumn(name = "cost")
  private BigDecimal cost;

  @JoinColumn(name = "purchased_at")
  private Instant purchasedAt;

  @ManyToMany
  @JoinTable(
      name = "ordered_certificates",
      joinColumns = @JoinColumn(name = "order_id"),
      inverseJoinColumns = @JoinColumn(name = "certificate_id"))
  private List<GiftCertificate> certificates;

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public Instant getPurchasedAt() {
    return purchasedAt;
  }

  public void setPurchasedAt(Instant purchasedAt) {
    this.purchasedAt = purchasedAt;
  }

  public List<GiftCertificate> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<GiftCertificate> certificates) {
    this.certificates = certificates;
  }
}

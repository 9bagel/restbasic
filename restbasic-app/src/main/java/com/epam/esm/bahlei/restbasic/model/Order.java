package com.epam.esm.bahlei.restbasic.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class Order {
  private long id;
  private long userId;
  private BigDecimal cost;
  private OffsetDateTime purchasedAt;
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

  public OffsetDateTime getPurchasedAt() {
    return purchasedAt;
  }

  public void setPurchasedAt(OffsetDateTime purchasedAt) {
    this.purchasedAt = purchasedAt;
  }

  public List<GiftCertificate> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<GiftCertificate> certificates) {
    this.certificates = certificates;
  }
}

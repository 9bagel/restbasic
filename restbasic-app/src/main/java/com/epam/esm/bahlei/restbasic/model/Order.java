package com.epam.esm.bahlei.restbasic.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class Order {
  private long id;
  private BigDecimal cost;
  private OffsetDateTime purchaseDate;
  private List<GiftCertificate> certificates;

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

  public OffsetDateTime getPurchaseDate() {
    return purchaseDate;
  }

  public void setPurchaseDate(OffsetDateTime purchaseDate) {
    this.purchaseDate = purchaseDate;
  }

  public List<GiftCertificate> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<GiftCertificate> certificates) {
    this.certificates = certificates;
  }
}

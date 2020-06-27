package com.epam.esm.bahlei.restbasic.controller.dto;

import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Order;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class OrderDTO {
  public long id;
  public BigDecimal cost;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:X")
  public OffsetDateTime purchaseDate;

  public List<GiftCertificate> certificates;

  public OrderDTO(Order order) {
    this.id = order.getId();
    this.cost = order.getCost();
    this.purchaseDate = order.getPurchaseDate();
    this.certificates = order.getCertificates();
  }

  public OrderDTO() {
  }
}

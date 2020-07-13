package com.epam.esm.bahlei.restbasic.controller.refdto;

import com.epam.esm.bahlei.restbasic.model.Order;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class OrderRefDTO {
  public long id;
  public BigDecimal cost;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:X")
  public OffsetDateTime purchaseDate;

  public OrderRefDTO(Order order) {
    this.id = order.getId();
    this.cost = order.getCost();
    this.purchaseDate = order.getPurchaseDate();
  }

  public OrderRefDTO() {}
}
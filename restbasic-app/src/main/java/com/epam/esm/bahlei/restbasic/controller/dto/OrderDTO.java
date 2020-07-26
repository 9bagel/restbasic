package com.epam.esm.bahlei.restbasic.controller.dto;

import com.epam.esm.bahlei.restbasic.controller.RefDTO;
import com.epam.esm.bahlei.restbasic.model.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderDTO extends RepresentationModel<OrderDTO> {
  public long id;
  public long userId;
  public BigDecimal cost;

  @JsonFormat public Instant purchasedAt;

  public List<RefDTO> certificates;

  public OrderDTO(Order order) {
    this.id = order.getId();
    this.cost = order.getCost();
    this.purchasedAt = order.getPurchasedAt();
    this.certificates =
        order.getCertificates().stream()
            .map(certificate -> new RefDTO(id, "Order# " + id))
            .collect(toList());
    this.userId = order.getUserId();
  }

  public OrderDTO() {}
}

package com.epam.esm.bahlei.restbasic.controller.dto;

import com.epam.esm.bahlei.restbasic.controller.refdto.RefDTO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class OrderDTO extends RepresentationModel<OrderDTO> {
  public long id;
  public long userId;
  public BigDecimal cost;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:X")
  public OffsetDateTime purchasedAt;

  public List<RefDTO> certificates;

  public OrderDTO(Order order) {
    this.id = order.getId();
    this.cost = order.getCost();
    this.purchasedAt = order.getPurchasedAt();
    this.certificates = order.getCertificates().stream().map(RefDTO::new).collect(toList());
    this.userId = order.getUserId();
  }

  public OrderDTO() {}
}

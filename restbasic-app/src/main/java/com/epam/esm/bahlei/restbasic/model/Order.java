package com.epam.esm.bahlei.restbasic.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
  private long id;
  private BigDecimal cost;
  private LocalDateTime purchaseDate;
  private List<GiftCertificate> certificates;
}

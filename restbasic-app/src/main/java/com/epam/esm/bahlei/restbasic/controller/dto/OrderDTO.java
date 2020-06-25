package com.epam.esm.bahlei.restbasic.controller.dto;

import com.epam.esm.bahlei.restbasic.model.GiftCertificate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private long id;
    private BigDecimal cost;
    private LocalDateTime purchaseDate;
    private List<GiftCertificate> certificates;
}

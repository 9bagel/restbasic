package com.epam.esm.bahlei.restbasic.controller.dto;

import com.epam.esm.bahlei.restbasic.model.GiftCertificate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    public long id;
    public BigDecimal cost;
    public LocalDateTime purchaseDate;
    public List<GiftCertificate> certificates;
}

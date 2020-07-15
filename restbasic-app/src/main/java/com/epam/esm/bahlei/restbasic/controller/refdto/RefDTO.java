package com.epam.esm.bahlei.restbasic.controller.refdto;

import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Order;
import com.epam.esm.bahlei.restbasic.model.User;
import org.springframework.hateoas.RepresentationModel;

public class RefDTO extends RepresentationModel<RefDTO> {
  private long id;
  private String displayName;

  public RefDTO() {}

  public RefDTO(User user) {
    this.id = user.getId();
    this.displayName = user.getFirstName() + " " + user.getSecondName();
  }

  public RefDTO(Order order) {
    this.id = order.getId();
    this.displayName = "Order №" + order.getId();
  }

  public RefDTO(GiftCertificate certificate) {
    this.id = certificate.getId();
    this.displayName = certificate.getName();
  }

  public long getId() {
    return id;
  }

  public String getDisplayName() {
    return displayName;
  }
}
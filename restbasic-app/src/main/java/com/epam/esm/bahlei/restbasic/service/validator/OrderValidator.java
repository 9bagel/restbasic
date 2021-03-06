package com.epam.esm.bahlei.restbasic.service.validator;

import com.epam.esm.bahlei.restbasic.dao.GiftCertificateDAO;
import com.epam.esm.bahlei.restbasic.dao.UserDAO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderValidator {

  private final GiftCertificateDAO certificateDAO;
  private final UserDAO userDAO;

  @Autowired
  public OrderValidator(GiftCertificateDAO certificateDAO, UserDAO userDAO) {
    this.certificateDAO = certificateDAO;
    this.userDAO = userDAO;
  }

  public List<String> validate(Order order) {
    List<String> errors = new ArrayList<>();

    validateCost(order.getCost(), errors);
    validateCertificates(order.getCertificates(), errors);
    validateUserId(order.getUserId(), errors);
    return errors;
  }

  private void validateUserId(long userId, List<String> errors) {
    if (!userDAO.get(userId).isPresent()) {
      errors.add("User doesn't exist");
    }
  }

  private void validateCost(BigDecimal cost, List<String> errors) {
    if (cost == null) {
      return;
    }
    if (cost.compareTo(BigDecimal.ZERO) < 0) {
      errors.add("Order price can't be negative.");
    }
  }

  private void validateCertificates(List<GiftCertificate> certificates, List<String> errors) {
    if (certificates == null) {
      errors.add("There must be at least 1 certificate in the order");
      return;
    }
    certificates.stream()
        .filter(certificate -> !certificateDAO.get(certificate.getId()).isPresent())
        .forEach(
            certificate -> errors.add("There is no certificate with id " + certificate.getId()));
  }
}

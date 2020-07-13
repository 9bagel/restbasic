package com.epam.esm.bahlei.restbasic.service.validator;

import com.epam.esm.bahlei.restbasic.dao.certificate.GiftCertificateDAO;
import com.epam.esm.bahlei.restbasic.dao.order.OrderDAO;
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
  private final OrderDAO orderDAO;
  private final UserValidator userValidator;

  @Autowired
  public OrderValidator(
      GiftCertificateDAO certificateDAO, OrderDAO orderDAO, UserValidator userValidator) {
    this.certificateDAO = certificateDAO;
    this.orderDAO = orderDAO;
    this.userValidator = userValidator;
  }

  public List<String> validate(Order order) {
    List<String> errors = new ArrayList<>();
    validateCost(order.getCost(), errors);
    validateCertificates(order.getCertificates(), errors);
    errors.addAll(userValidator.validate(order.getUserId()));
    return errors;
  }

  public List<String> validate(long orderId, long userId) {
    List<String> errors = new ArrayList<>();
    validateOrderId(orderId, errors);
    errors.addAll(userValidator.validate(userId));
    return errors;
  }

  public List<String> validate(long orderId) {
    List<String> errors = new ArrayList<>();
    validateOrderId(orderId, errors);
    return errors;
  }

  private void validateOrderId(long orderId, List<String> errors) {
    if (orderId <= 0) {
      errors.add("orderId should be > 0");
      return;
    }
    if (!orderDAO.get(orderId).isPresent()) {
      errors.add("There is no order with id " + orderId);
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

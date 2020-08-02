package com.epam.esm.bahlei.restbasic.service.impl;

import com.epam.esm.bahlei.restbasic.dao.GiftCertificateDAO;
import com.epam.esm.bahlei.restbasic.dao.OrderDAO;
import com.epam.esm.bahlei.restbasic.dao.TagDAO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Order;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Tag;
import com.epam.esm.bahlei.restbasic.security.jwt.JwtUser;
import com.epam.esm.bahlei.restbasic.service.OrderService;
import com.epam.esm.bahlei.restbasic.service.supplies.AuthoritiesChecker;
import com.epam.esm.bahlei.restbasic.service.validator.OrderValidator;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.bahlei.restbasic.model.Role.ADMIN;
import static java.util.stream.Collectors.toList;

@Service
public class OrderServiceImpl implements OrderService {

  private final OrderDAO orderDAO;
  private final GiftCertificateDAO certificateDAO;
  private final OrderValidator validator;
  private final TagDAO tagDAO;
  private final AuthoritiesChecker authoritiesChecker;

  @Autowired
  public OrderServiceImpl(
      OrderDAO orderDAO,
      GiftCertificateDAO certificateDAO,
      OrderValidator validator,
      TagDAO tagDAO,
      AuthoritiesChecker authoritiesChecker) {
    this.orderDAO = orderDAO;
    this.certificateDAO = certificateDAO;
    this.validator = validator;
    this.tagDAO = tagDAO;
    this.authoritiesChecker = authoritiesChecker;
  }

  @Override
  @Transactional
  public void save(Order order) {
    authoritiesChecker.check(order.getUserId());

    List<String> errors = validator.validate(order);

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    List<GiftCertificate> certificates = getCertificatesData(order.getCertificates());
    order.setCost(calculateCost(certificates));

    orderDAO.save(order);
  }

  private BigDecimal calculateCost(List<GiftCertificate> certificates) {
    return certificates.stream()
        .map(GiftCertificate::getPrice)
        .reduce(BigDecimal::add)
        .orElse(BigDecimal.ZERO);
  }

  private List<GiftCertificate> getCertificatesData(List<GiftCertificate> certificates) {
    return certificates.stream()
        .map(certificate -> certificateDAO.get(certificate.getId()).orElse(new GiftCertificate()))
        .collect(toList());
  }

  @Override
  public List<Order> getUserOrders(long userId, Pageable pageable) {
    authoritiesChecker.check(userId);

    return orderDAO.getUserOrders(userId, pageable);
  }

  @Override
  public Optional<Order> get(long orderId, long userId) {
    authoritiesChecker.check(userId);

    Optional<Order> order = orderDAO.get(userId, orderId);
    order.ifPresent(this::setOrderedCertificates);
    return order;
  }

  private void setOrderedCertificates(Order order) {
    List<GiftCertificate> certificates = certificateDAO.getCertificatesByOrderId(order.getId());
    certificates.forEach(this::setCertificateTags);
    order.setCertificates(certificates);
  }

  private void setCertificateTags(GiftCertificate giftCertificate) {
    List<Tag> tags = tagDAO.getCertificateTags(giftCertificate.getId());
    giftCertificate.setTags(tags);
  }
}

package com.epam.esm.bahlei.restbasic.service.order;

import com.epam.esm.bahlei.restbasic.dao.certificate.GiftCertificateDAO;
import com.epam.esm.bahlei.restbasic.dao.order.OrderDAO;
import com.epam.esm.bahlei.restbasic.dao.tag.TagDAO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Order;
import com.epam.esm.bahlei.restbasic.model.Tag;
import com.epam.esm.bahlei.restbasic.service.validator.OrderValidator;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class OrderServiceImpl implements OrderService {

  private final OrderDAO orderDAO;
  private final GiftCertificateDAO certificateDAO;
  private final OrderValidator validator;
  private final TagDAO tagDAO;

  @Autowired
  public OrderServiceImpl(OrderDAO orderDAO, GiftCertificateDAO certificateDAO,
                          OrderValidator validator, TagDAO tagDAO) {
    this.orderDAO = orderDAO;
    this.certificateDAO = certificateDAO;
    this.validator = validator;
    this.tagDAO = tagDAO;
  }

  @Override
  @Transactional
  public void save(Order order) {
    List<String> errors = validator.validate(order);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    List<GiftCertificate> certificates = getCertificatesData(order.getCertificates());
    if (order.getCost() == null) {
      order.setCost(calculateCost(certificates));
    }

    orderDAO.save(order);
    orderDAO.saveOrderedCertificates(order.getCertificates(), order.getId());
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
  public List<Order> getAll() {
    List<Order> orders = orderDAO.getAll();

    orders.forEach(this::setOrderedCertificates);
    return orders;
  }

  @Override
  public Optional<Order> get(long id) {
    Optional<Order> order = orderDAO.get(id);
    order.ifPresent(this::setOrderedCertificates);
    return order;
  }

  private void setOrderedCertificates(Order order) {
    List<GiftCertificate> certificates = certificateDAO.getOrderedCertificates(order.getId());
    certificates.forEach(this::setCertificateTags);
    order.setCertificates(certificates);
  }

  private void setCertificateTags(GiftCertificate giftCertificate) {
    List<Tag> tags = tagDAO.getCertificateTags(giftCertificate.getId());
    giftCertificate.setTags(tags);
  }
}
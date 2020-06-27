package com.epam.esm.bahlei.restbasic.dao.certificate;

import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.service.supplies.Criteria;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDAO {

  Optional<GiftCertificate> get(long giftCertificateId);

  List<GiftCertificate> getAll(Criteria criteria);

  void save(GiftCertificate giftCertificate);

  void delete(long id);

  void update(GiftCertificate giftCertificate);

  Optional<GiftCertificate> getByName(String tagName);

  List<GiftCertificate> getOrderedCertificates(long id);
}

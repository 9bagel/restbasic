package com.epam.esm.bahlei.restbasic.service.certificate;

import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.service.supplies.Criteria;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateService {

  Optional<GiftCertificate> get(long giftCertificateId);

  List<GiftCertificate> getAll(Criteria criteria);

  void save(GiftCertificate giftCertificate);

  void delete(long giftCertificateId);

  void update(GiftCertificate giftCertificate);
}

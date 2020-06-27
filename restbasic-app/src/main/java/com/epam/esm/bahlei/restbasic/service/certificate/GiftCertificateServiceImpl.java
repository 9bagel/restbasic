package com.epam.esm.bahlei.restbasic.service.certificate;

import com.epam.esm.bahlei.restbasic.dao.certificate.GiftCertificateDAO;
import com.epam.esm.bahlei.restbasic.dao.tag.TagDAO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Tag;
import com.epam.esm.bahlei.restbasic.service.supplies.Criteria;
import com.epam.esm.bahlei.restbasic.service.validator.CertificateValidator;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

  private final GiftCertificateDAO giftCertificateDAO;
  private final TagDAO tagDAO;
  private final CertificateValidator certificateValidator;

  @Autowired
  public GiftCertificateServiceImpl(
      GiftCertificateDAO giftCertificateDAO,
      TagDAO tagDAO,
      CertificateValidator certificateValidator) {
    this.giftCertificateDAO = giftCertificateDAO;
    this.tagDAO = tagDAO;
    this.certificateValidator = certificateValidator;
  }

  @Transactional
  @Override
  public Optional<GiftCertificate> get(long giftCertificateId) {
    Optional<GiftCertificate> optional = giftCertificateDAO.get(giftCertificateId);
    if (!optional.isPresent()) {
      return Optional.empty();
    }
    setCertificateTags(optional.get());
    return optional;
  }

  @Override
  public List<GiftCertificate> getAll(Criteria criteria) {

    List<GiftCertificate> certificates = giftCertificateDAO.getAll(criteria);
    certificates.forEach(this::setCertificateTags);
    return certificates;
  }

  private void setCertificateTags(GiftCertificate giftCertificate) {
    List<Tag> tags = tagDAO.getCertificateTags(giftCertificate.getId());
    giftCertificate.setTags(tags);
  }

  @Transactional
  @Override
  public void save(GiftCertificate giftCertificate) {
    List<String> errors = certificateValidator.validateForSave(giftCertificate);

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    giftCertificateDAO.save(giftCertificate);
    saveCertificateTags(giftCertificate);
  }

  @Transactional
  @Override
  public void delete(long giftCertificateId) {
    tagDAO.deleteCertificateTags(giftCertificateId);
    giftCertificateDAO.delete(giftCertificateId);
  }

  @Transactional
  @Override
  public void update(GiftCertificate giftCertificate) {
    List<String> errors = certificateValidator.validateForUpdate(giftCertificate);

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    saveCertificateTags(giftCertificate);
    giftCertificateDAO.update(giftCertificate);
  }

  private void saveCertificateTags(GiftCertificate giftCertificate) {
    List<Tag> tags = giftCertificate.getTags();

    if (tags.isEmpty()) {
      return;
    }
    tagDAO.deleteCertificateTags(giftCertificate.getId());

    List<Tag> nonExistingTags =
        tags.stream().filter(tag -> !tagDAO.getByName(tag.getName()).isPresent()).collect(toList());
    nonExistingTags.forEach(tagDAO::save);

    tags = tags.stream().map(tag -> tagDAO.getByName(tag.getName()).orElse(tag)).collect(toList());
    tags.forEach(tag -> tagDAO.saveCertificateTag(giftCertificate.getId(), tag.getId()));
  }
}

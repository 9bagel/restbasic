package com.epam.esm.bahlei.restbasic.service.certificate;

import com.epam.esm.bahlei.restbasic.dao.certificate.GiftCertificateDAO;
import com.epam.esm.bahlei.restbasic.dao.tag.TagDAO;
import com.epam.esm.bahlei.restbasic.dao.user.UserDAO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Tag;
import com.epam.esm.bahlei.restbasic.service.supplies.Criteria;
import com.epam.esm.bahlei.restbasic.service.utils.ServiceUtils;
import com.epam.esm.bahlei.restbasic.service.validator.CertificateValidator;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

  private final GiftCertificateDAO giftCertificateDAO;
  private final TagDAO tagDAO;
  private final UserDAO userDAO;
  private final CertificateValidator certificateValidator;

  @Autowired
  public GiftCertificateServiceImpl(
      GiftCertificateDAO giftCertificateDAO,
      TagDAO tagDAO,
      UserDAO userDAO,
      CertificateValidator certificateValidator) {
    this.giftCertificateDAO = giftCertificateDAO;
    this.tagDAO = tagDAO;
    this.userDAO = userDAO;
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
  public List<GiftCertificate> getAll(Criteria criteria, Pageable pageable) {
    List<GiftCertificate> certificates = giftCertificateDAO.getAll(criteria, pageable);
    certificates.forEach(this::setCertificateTags);
    return certificates;
  }

  private void setCertificateTags(GiftCertificate giftCertificate) {
    List<Tag> tags = tagDAO.getCertificateTags(giftCertificate.getId());
    giftCertificate.setTags(tags);
  }

  @Override
  public Optional<GiftCertificate> getFavouriteUserCertificate(long userId) {
    if (!userDAO.get(userId).isPresent()) {
      return Optional.empty();
    }
    Optional<GiftCertificate> optional = giftCertificateDAO.getFavouriteUserCertificate(userId);
    if (!optional.isPresent()) {
      return Optional.empty();
    }
    setCertificateTags(optional.get());
    return optional;
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  @Override
  public void save(GiftCertificate giftCertificate) {
    List<String> errors = certificateValidator.validate(giftCertificate);

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

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  @Override
  public void update(GiftCertificate giftCertificate) {
    List<String> errors = certificateValidator.validate(giftCertificate);
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

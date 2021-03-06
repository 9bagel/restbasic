package com.epam.esm.bahlei.restbasic.service.impl;

import com.epam.esm.bahlei.restbasic.dao.GiftCertificateDAO;
import com.epam.esm.bahlei.restbasic.dao.TagDAO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Tag;
import com.epam.esm.bahlei.restbasic.service.GiftCertificateService;
import com.epam.esm.bahlei.restbasic.service.UserService;
import com.epam.esm.bahlei.restbasic.service.supplies.AuthoritiesChecker;
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
  private final UserService userService;
  private final CertificateValidator certificateValidator;
  private final AuthoritiesChecker authoritiesChecker;

  @Autowired
  public GiftCertificateServiceImpl(
      GiftCertificateDAO giftCertificateDAO,
      TagDAO tagDAO,
      UserService userService,
      CertificateValidator certificateValidator,
      AuthoritiesChecker authoritiesChecker) {
    this.giftCertificateDAO = giftCertificateDAO;
    this.tagDAO = tagDAO;
    this.userService = userService;
    this.certificateValidator = certificateValidator;
    this.authoritiesChecker = authoritiesChecker;
  }

  @Override
  public Optional<GiftCertificate> get(long giftCertificateId) {
    return giftCertificateDAO.get(giftCertificateId);
  }

  @Override
  public List<GiftCertificate> getAll(Criteria criteria, Pageable pageable) {
    return giftCertificateDAO.getAll(criteria, pageable);
  }

  @Override
  public Optional<GiftCertificate> getFavouriteUserCertificate(long userId) {
    authoritiesChecker.check(userId);

    if (!userService.get(userId).isPresent()) {
      return Optional.empty();
    }
    return giftCertificateDAO.getFavouriteUserCertificate(userId);
  }

  @Transactional
  @Override
  public void save(GiftCertificate giftCertificate) {
    List<String> errors = certificateValidator.validate(giftCertificate);

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    getTagIds(giftCertificate);
    giftCertificateDAO.save(giftCertificate);
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
    List<String> errors = certificateValidator.validate(giftCertificate);

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }

    getTagIds(giftCertificate);
    giftCertificateDAO.update(giftCertificate);
  }

  private void getTagIds(GiftCertificate giftCertificate) {
    List<Tag> tags = giftCertificate.getTags();

    if (tags.isEmpty()) {
      return;
    }

    List<Tag> nonExistingTags = getNonExistingTags(tags);
    nonExistingTags.forEach(tagDAO::save);

    giftCertificate.setTags(getTagsWIthId(tags));
  }

  private List<Tag> getNonExistingTags(List<Tag> tags) {
    return tags.stream()
        .filter(tag -> !tagDAO.getByName(tag.getName()).isPresent())
        .collect(toList());
  }

  private List<Tag> getTagsWIthId(List<Tag> tags) {
    return tags.stream().map(tag -> tagDAO.getByName(tag.getName()).orElse(tag)).collect(toList());
  }
}

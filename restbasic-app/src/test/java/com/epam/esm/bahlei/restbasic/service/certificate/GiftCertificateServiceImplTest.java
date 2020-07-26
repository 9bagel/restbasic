package com.epam.esm.bahlei.restbasic.service.certificate;

import com.epam.esm.bahlei.restbasic.dao.GiftCertificateDAO;
import com.epam.esm.bahlei.restbasic.dao.TagDAO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Tag;
import com.epam.esm.bahlei.restbasic.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.bahlei.restbasic.service.validator.CertificateValidator;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {
  @Mock private GiftCertificateDAO certificateDAO;
  @Mock private TagDAO tagDAO;
  @Mock private CertificateValidator validator;
  @InjectMocks private GiftCertificateServiceImpl certificateService;

  @Test
  void get_NonExistingId_EmptyOptional() {
    when(certificateDAO.get(1)).thenReturn(Optional.empty());

    Optional<GiftCertificate> actual = certificateService.get(1);

    assertThat(actual).isEmpty();
  }

  @Test
  void get_ValidId_OK() {
    Optional<GiftCertificate> expected = Optional.of(getValidCertificate());
    List<Tag> tags = getValidTags();
    when(certificateDAO.get(1)).thenReturn(expected);
    when(tagDAO.getCertificateTags(1)).thenReturn(tags);

    Optional<GiftCertificate> actual = certificateService.get(1);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void update_InvalidCertificate_Error() {
    GiftCertificate certificate = getValidCertificate();
    when(validator.validate(certificate)).thenReturn(singletonList("error"));

    assertThrows(ValidationException.class, () -> certificateService.update(certificate));
  }

  @Test
  void update_ValidCertificate_OK() {
    GiftCertificate certificate = getValidCertificate();
    when(validator.validate(certificate)).thenReturn(emptyList());

    assertThatCode(() -> certificateService.update(certificate)).doesNotThrowAnyException();
  }

  @Test
  void save_InvalidCertificate_Error() {
    GiftCertificate certificate = getValidCertificate();
    when(validator.validate(certificate)).thenReturn(singletonList("error"));

    assertThrows(ValidationException.class, () -> certificateService.save(certificate));
  }

  @Test
  void save_ValidCertificate_OK() {
    GiftCertificate certificate = getValidCertificate();
    when(validator.validate(certificate)).thenReturn(emptyList());

    assertThatCode(() -> certificateService.save(certificate)).doesNotThrowAnyException();
  }

  private GiftCertificate getValidCertificate() {
    GiftCertificate giftCertificate = new GiftCertificate();
    giftCertificate.setId(1);
    giftCertificate.setDescription("desc");
    giftCertificate.setPrice(BigDecimal.valueOf(100));
    giftCertificate.setDuration(10);
    giftCertificate.setName("certificate");
    giftCertificate.setCreatedAt(Instant.now());
    giftCertificate.setModifiedAt(Instant.now());
    giftCertificate.setTags(getValidTags());

    return giftCertificate;
  }

  private List<Tag> getValidTags() {
    List<Tag> tags = new ArrayList<>();
    Tag tag = new Tag();
    tag.setId(1);
    tag.setName("validName");
    return tags;
  }
}

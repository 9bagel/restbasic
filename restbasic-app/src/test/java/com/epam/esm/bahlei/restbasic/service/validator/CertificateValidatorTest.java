package com.epam.esm.bahlei.restbasic.service.validator;

import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CertificateValidatorTest {
  private final CertificateValidator validator = new CertificateValidator();

  @Test
  void validate_NullName_Error() {
    GiftCertificate certificate = getValidCertificate();
    certificate.setName(null);

    List<String> errors = validator.validate(certificate);

    assertEquals("Certificate name should not be empty", errors.get(0));
  }

  @Test
  void validate_EmptyName_Error() {
    GiftCertificate certificate = getValidCertificate();
    certificate.setName("   ");

    List<String> errors = validator.validate(certificate);
    assertThat(errors).containsOnly("Certificate name should not be empty");
  }

  @Test
  void validate_NullDescription_Error() {
    GiftCertificate certificate = getValidCertificate();
    certificate.setDescription(null);

    List<String> errors = validator.validate(certificate);

    assertThat(errors).containsOnly("Certificate description should not be empty");
  }

  @Test
  void validate_EmptyDescription_Error() {
    GiftCertificate certificate = getValidCertificate();
    certificate.setDescription("   ");

    List<String> errors = validator.validate(certificate);

    assertThat(errors).containsOnly("Certificate description should not be empty");
  }

  @Test
  void validate_LowerThanZeroPrice_Error() {
    GiftCertificate certificate = getValidCertificate();
    certificate.setPrice(BigDecimal.valueOf(-1));

    List<String> errors = validator.validate(certificate);

    assertThat(errors).containsOnly("Price can't be negative.");
  }

  @Test
  void validate_ZeroPrice_Error() {
    GiftCertificate certificate = getValidCertificate();
    certificate.setPrice(BigDecimal.valueOf(0));

    List<String> errors = validator.validate(certificate);

    assertThat(errors).isEmpty();
  }

  @Test
  void validate_OnePrice_Error() {
    GiftCertificate certificate = getValidCertificate();
    certificate.setPrice(BigDecimal.valueOf(1));

    List<String> errors = validator.validate(certificate);

    assertThat(errors).isEmpty();
  }

  @Test
  void validate_NullPrice_Error() {
    GiftCertificate certificate = getValidCertificate();
    certificate.setPrice(null);

    List<String> errors = validator.validate(certificate);

    assertThat(errors).containsOnly("Price can't be null.");
  }

  @Test
  void validate_LowerThanZeroDuration_Error() {
    GiftCertificate certificate = getValidCertificate();
    certificate.setDuration(-1);

    List<String> errors = validator.validate(certificate);

    assertEquals("Duration can't be lower than 0.", errors.get(0));
  }

  @Test
  void validate_ZeroDuration_Ok() {
    GiftCertificate certificate = getValidCertificate();
    certificate.setDuration(0);

    List<String> errors = validator.validate(certificate);

    assertTrue(errors.isEmpty());
  }

  @Test
  void validate_OneDuration_Ok() {
    GiftCertificate certificate = getValidCertificate();
    certificate.setDuration(1);

    List<String> errors = validator.validate(certificate);

    assertTrue(errors.isEmpty());
  }
  @Test
  void validate_ValidObject_OK() {
    GiftCertificate certificate = getValidCertificate();

    List<String> errors = validator.validate(certificate);

    assertThat(errors).isEmpty();
  }

  private GiftCertificate getValidCertificate() {
    GiftCertificate giftCertificate = new GiftCertificate();
    giftCertificate.setId(1);
    giftCertificate.setDescription("desc");
    giftCertificate.setPrice(BigDecimal.valueOf(100));
    giftCertificate.setDuration(10);
    giftCertificate.setName("certificate");
    giftCertificate.setCreatedAt(LocalDateTime.now());
    giftCertificate.setModifiedAt(LocalDateTime.now());

    return giftCertificate;
  }
}

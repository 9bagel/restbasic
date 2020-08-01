package com.epam.esm.bahlei.restbasic.service.validator;

import com.epam.esm.bahlei.restbasic.dao.GiftCertificateDAO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

@Component
public class CertificateValidator {
  private final GiftCertificateDAO certificateDAO;

  public CertificateValidator(GiftCertificateDAO certificateDAO) {
    this.certificateDAO = certificateDAO;
  }

  public void validate(GiftCertificate certificate) {
    List<String> errors = new ArrayList<>();
    if (certificate.getId() == 0 && certificateDAO.getByName(certificate.getName()).isPresent()) {
      errors.add(String.format("Certificate with name %s already exists.", certificate.getName()));
    }

    validateName(certificate.getName(), errors);
    validateDescription(certificate.getDescription(), errors);
    validatePrice(certificate.getPrice(), errors);
    validateDuration(certificate.getDuration(), errors);

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
  }

  private void validateName(String name, List<String> errorMessages) {
    if (isNullOrEmpty(name)) {
      errorMessages.add("Certificate name should not be empty");
      return;
    }
    if (name.length() > 255) {
      errorMessages.add("Length of the certificate name cannot be more than 255.");
    }
  }

  private void validateDescription(String description, List<String> errorMessages) {
    if (isNullOrEmpty(description)) {
      errorMessages.add("Certificate description should not be empty");
      return;
    }
    if (description.length() > 255) {
      errorMessages.add("Length of the description cannot be more than 255.");
    }
  }

  private void validatePrice(BigDecimal price, List<String> errorMessages) {
    if (price == null) {
      errorMessages.add("Price can't be null.");
      return;
    }
    if (price.compareTo(BigDecimal.ZERO) < 0) {
      errorMessages.add("Price can't be negative.");
    }
  }

  private void validateDuration(int duration, List<String> errorMessages) {
    if (duration < 0) {
      errorMessages.add("Duration can't be lower than 0.");
    }
  }
}

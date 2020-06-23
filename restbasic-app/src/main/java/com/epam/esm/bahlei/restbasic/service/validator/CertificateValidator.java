package com.epam.esm.bahlei.restbasic.service.validator;

import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class CertificateValidator {

  public CertificateValidator() {}

  public List<String> validate(GiftCertificate certificate) {
    List<String> errors = new ArrayList<>();

    validateName(certificate.getName(), errors);
    validateDescription(certificate.getDescription(), errors);
    validatePrice(certificate.getPrice(), errors);
    validateDuration(certificate.getDuration(), errors);

    return errors;
  }

  private void validateName(String name, List<String> errorMessages) {
    if (name == null || name.trim().isEmpty()) {
      errorMessages.add("Certificate name should not be empty");
    }
  }

  private void validateDescription(String description, List<String> errorMessages) {
    if (description == null || description.trim().isEmpty()) {
      errorMessages.add("Certificate description should not be empty");
    }
  }

  private void validatePrice(BigDecimal price, List<String> errorMessages) {
    if (price == null) {
      errorMessages.add("Price can't be null.");
    } else if (price.compareTo(BigDecimal.ZERO) < 0) {
      errorMessages.add("Price can't be negative.");
    }
  }

  private void validateDuration(int duration, List<String> errorMessages) {
    if (duration < 0) {
      errorMessages.add("Duration can't be lower than 0.");
    }
  }
}

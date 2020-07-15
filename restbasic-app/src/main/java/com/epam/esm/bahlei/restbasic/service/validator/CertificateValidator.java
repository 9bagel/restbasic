package com.epam.esm.bahlei.restbasic.service.validator;

import com.epam.esm.bahlei.restbasic.dao.certificate.GiftCertificateDAO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class CertificateValidator {
  private final GiftCertificateDAO certificateDAO;

  public CertificateValidator(GiftCertificateDAO certificateDAO) {
    this.certificateDAO = certificateDAO;
  }

  public List<String> validate(GiftCertificate certificate) {
    List<String> errors = new ArrayList<>();

    if (certificate.getId() == 0) {
      validateNameForSave(certificate.getName(), errors);
    } else {
      validateNameForUpdate(certificate.getName(), errors);
    }
    validateDescription(certificate.getDescription(), errors);
    validatePrice(certificate.getPrice(), errors);
    validateDuration(certificate.getDuration(), errors);

    return errors;
  }

  private void validateNameForUpdate(String name, List<String> errorMessages) {
    validateName(name, errorMessages);
  }
//Перенести этот if в метод Validate
  private void validateNameForSave(String name, List<String> errorMessages) {
    validateName(name, errorMessages);
    if (certificateDAO.getByName(name).isPresent()) {
      errorMessages.add(String.format("Certificate with name %s already exists.", name));
    }
  }

  private void validateName(String name, List<String> errorMessages) {
    if (name == null || name.trim().isEmpty()) {
      errorMessages.add("Certificate name should not be empty");
      return;
    }
    if (name.length() > 255) {
      errorMessages.add("Length of the certificate name cannot be more than 255.");
    }
  }

  private void validateDescription(String description, List<String> errorMessages) {
    if (description == null || description.trim().isEmpty()) {
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

package com.epam.esm.bahlei.restbasic.service.validator.exception;

import java.util.List;

public class ValidationException extends RuntimeException {
  private final List<String> errors;
  static final long serialVersionUID = -4746365774470841353L;

  public ValidationException(List<String> validationErrors) {
    errors = validationErrors;
  }

  public List<String> getErrors() {
    return errors;
  }
}

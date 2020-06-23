package com.epam.esm.bahlei.restbasic.config.exception.response;

import java.util.Collections;
import java.util.List;

public class ErrorResponse {
  public final List<String> errors;

  public ErrorResponse(List<String> errors) {
    this.errors = errors;
  }

  public ErrorResponse(String error) {
    errors = Collections.singletonList(error);
  }
}

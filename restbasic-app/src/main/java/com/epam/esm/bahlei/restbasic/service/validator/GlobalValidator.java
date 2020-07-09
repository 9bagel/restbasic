package com.epam.esm.bahlei.restbasic.service.validator;

import java.util.ArrayList;
import java.util.List;

public class GlobalValidator {

  public static List<String> validatePagination(int page, int size) {
    List<String> errorMessages = new ArrayList<>();
    if (page <= 0) {
      errorMessages.add("page should be > 0");
    }

    if (size <= 0) {
      errorMessages.add("size should be > 0");
    }

    return errorMessages;
  }
}

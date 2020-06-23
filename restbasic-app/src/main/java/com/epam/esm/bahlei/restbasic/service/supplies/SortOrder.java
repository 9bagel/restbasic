package com.epam.esm.bahlei.restbasic.service.supplies;

public enum SortOrder {
  ASC,
  DESC;

  static SortOrder fromString(String input) {
    switch (input) {
      case "-":
        return DESC;
      case "+":
      default:
        return ASC;
    }
  }
}

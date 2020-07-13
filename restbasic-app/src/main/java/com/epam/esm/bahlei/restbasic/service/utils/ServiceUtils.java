package com.epam.esm.bahlei.restbasic.service.utils;

public class ServiceUtils {
  public static long getOffset(int page, int size) {
    return size * (page - 1);
  }
}

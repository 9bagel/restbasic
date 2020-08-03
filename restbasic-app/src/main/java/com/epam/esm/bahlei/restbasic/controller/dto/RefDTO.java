package com.epam.esm.bahlei.restbasic.controller.dto;

public class RefDTO {
  public long id;
  public String displayName;

  public RefDTO() {}

  public RefDTO(long id, String displayName) {
    this.id = id;
    this.displayName = displayName;
  }
}

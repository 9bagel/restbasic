package com.epam.esm.bahlei.restbasic.controller.refdto;

public class RefDTO {
  private long id;
  private String displayName;

  public RefDTO() {}

  public RefDTO(long id, String displayName) {
    this.id = id;
    this.displayName = displayName;
  }

  public long getId() {
    return id;
  }

  public String getDisplayName() {
    return displayName;
  }
}
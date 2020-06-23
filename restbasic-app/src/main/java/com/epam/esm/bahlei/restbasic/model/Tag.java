package com.epam.esm.bahlei.restbasic.model;

import com.google.common.base.Objects;
import org.springframework.stereotype.Component;

@Component
public class Tag {
  private long id;
  private String name;

  public Tag() {
  }

  public Tag(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Tag(String name) {
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tag)) return false;
    Tag tag = (Tag) o;
    return id == tag.id && Objects.equal(name, tag.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, name);
  }
}

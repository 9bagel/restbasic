package com.epam.esm.bahlei.restbasic.model;

import javax.persistence.*;

@Entity
@Table(name = "tags")
public class Tag {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_generator")
  @SequenceGenerator(name = "tag_generator", sequenceName = "tags_id_seq", allocationSize = 1)
  private long id;

  @Column(name = "name", unique = true, nullable = false)
  private String name;

  public Tag() {}

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
}

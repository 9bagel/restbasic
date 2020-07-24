package com.epam.esm.bahlei.restbasic.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_generator")
  @SequenceGenerator(name = "role_generator", sequenceName = "roles_id_seq", allocationSize = 1)
  private Long id;

  @Column(name = "name")
  private String name;

  @ManyToMany(mappedBy = "roles")
  private List<User> users;

  public Role() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

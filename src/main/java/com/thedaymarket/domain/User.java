package com.thedaymarket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

@Data
@Entity
public class User extends BaseEntity {
  private String role;
  private String firstName;
  private String lastName;

  @NaturalId(mutable = true)
  private String email;

  private String password;
  private String lastLoginDate;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private List<Reputation> reputation;
}

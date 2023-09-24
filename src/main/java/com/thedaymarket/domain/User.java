package com.thedaymarket.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

@Data
@Entity
public class User extends BaseEntity {
  private String firstName;
  private String lastName;

  @NaturalId(mutable = true)
  private String email;

  @Embedded private UserAuth auth;

  @Embedded private UserAddress address;

  private String profileImageName;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
  private Reputation reputation;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "belongsTo")
  private UserPoints userPoints;

  public BigDecimal getPoints() {
    return getUserPoints() != null ? getUserPoints().getCount() : new BigDecimal("0");
  }

  public boolean isFirstLogin() {
    return auth.getLastLoggedInAt() == null || role == null;
  }
}

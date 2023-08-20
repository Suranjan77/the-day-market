package com.thedaymarket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Reputation extends BaseEntity {
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  private Double reputationPoints;
}

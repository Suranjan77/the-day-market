package com.thedaymarket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Reputation extends BaseEntity {
  @OneToOne private User user;
  private Double reputationPoints;
}

package com.thedaymarket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class UserPoints extends BaseEntity {
  @OneToOne private User belongsTo;
  private BigDecimal count;
}

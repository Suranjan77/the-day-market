package com.thedaymarket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
@Entity
public class Market extends Base {
  private Long activeAuctions;
  private Long totalAuctions;

  @Enumerated(EnumType.STRING)
  private MarketStatus status;
}

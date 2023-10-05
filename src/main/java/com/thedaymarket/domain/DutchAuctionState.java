package com.thedaymarket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Entity
public class DutchAuctionState extends BaseEntity {
  private BigDecimal currentPoints;

  private LocalTime timerStartedAt;

  @OneToOne
  private Auction auction;

  private boolean expired;
}

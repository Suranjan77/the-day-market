package com.thedaymarket.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Entity
public class DutchAuctionState extends BaseEntity {
  private Long timerSeconds;
  private BigDecimal currentPoints;

  private LocalTime timerStartedAt;

  @ManyToOne
  private Auction auction;

  private boolean expired;
}

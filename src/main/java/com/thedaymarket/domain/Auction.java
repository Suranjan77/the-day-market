package com.thedaymarket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
public class Auction extends BaseEntity {
  private String description;

  private BigDecimal startingPrice;

  // For Dutch auction
  private BigDecimal decrementFactor;
  private Long decrementSeconds;

  @Enumerated(EnumType.STRING)
  private AuctionType type;

  private Long itemCount;

  @Enumerated(EnumType.STRING)
  private AuctionStatus status;

  private LocalDateTime scheduledDateTime;

  @ManyToOne private Market assignedMarket;

  @ManyToOne private Category category;
}

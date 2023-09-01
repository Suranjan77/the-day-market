package com.thedaymarket.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
public class Auction extends BaseEntity {
  private String title;
  private String description;

  private BigDecimal minAskPrice;

  // Percentage
  private Double decrementFactor;

  private Long decrementSeconds;

  @Enumerated(EnumType.STRING)
  private AuctionType type;

  private Long itemCount;

  @Enumerated(EnumType.STRING)
  private AuctionStatus status;

  private LocalDateTime scheduledDateTime;

  @ManyToOne private User seller;

  @ManyToOne private Market assignedMarket;

  @ManyToOne private Category category;

  private String imageName;

  private String imageUrl;
}

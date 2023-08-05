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
public class Auction extends Base {
  private LocalDateTime postedAt;
  private String description;
  private BigDecimal price;

  @Enumerated(EnumType.STRING)
  private AuctionType type;

  private Long itemCount;

  @Enumerated(EnumType.STRING)
  private AuctionStatus status;

  @ManyToOne private Market market;

  @ManyToOne private Category category;
}

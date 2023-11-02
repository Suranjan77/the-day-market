package com.thedaymarket.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import org.hibernate.annotations.OrderBy;

@Data
@Entity
public class Auction extends BaseEntity {
  private String title;
  private String description;

  private BigDecimal minAskPrice;

  private Double decrementFactor;

  private Long decrementSeconds;

  private BigDecimal decrementLimit;

  @Enumerated(EnumType.STRING)
  private AuctionType type;

  private Long itemCount;

  @Enumerated(EnumType.STRING)
  private AuctionStatus status;

  private LocalDate scheduledDate;

  @ManyToOne private User seller;

  @ManyToOne private Category category;

  @OneToMany(mappedBy = "auction")
  @OrderBy(clause = "amount DESC")
  private List<Bid> bids;

  @OneToOne private Bid winningBid;

  @OneToMany(mappedBy = "auction")
  private List<DutchAuctionState> dutchAuctionState;

  private String imageName;
}

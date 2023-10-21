package com.thedaymarket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.hibernate.annotations.Formula;

@Data
@Entity
public class Reputation extends BaseEntity {
  @OneToOne private User user;
  private double reputationPoints = 25.0;
  private int sellerRating;
  private float auctionRating;
  private long totalSold;
  private long totalAuctions;
  private boolean unCalculated;
}

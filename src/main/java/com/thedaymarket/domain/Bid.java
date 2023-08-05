package com.thedaymarket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Entity
public class Bid extends Base {
  @ManyToOne private Auction auction;

  @ManyToOne private User user;

  private BigDecimal amount;
}

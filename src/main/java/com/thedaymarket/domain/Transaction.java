package com.thedaymarket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Entity
public class Transaction extends Base {
  @ManyToOne private User buyer;

  @ManyToOne private User seller;

  private BigDecimal amount;

  @ManyToOne private Auction auction;
}

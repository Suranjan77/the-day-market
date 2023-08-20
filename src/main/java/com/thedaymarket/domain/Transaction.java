package com.thedaymarket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Entity
public class Transaction extends BaseEntity {
  @ManyToOne private User buyer;

  @ManyToOne private User seller;

  private BigDecimal amount;

  @ManyToOne private Auction auction;

  @Enumerated(EnumType.STRING)
  private TransactionType type;

  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod;
}

package com.thedaymarket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
@Entity
public class Rating extends BaseEntity {
  private Float stars;

  @Enumerated(EnumType.STRING)
  private RatingType type;

  private Long receiverId;
}

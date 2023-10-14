package com.thedaymarket.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"type", "receiverId", "rater_id"}))
@Entity
public class Rating extends BaseEntity {
  private Float stars;

  @Enumerated(EnumType.STRING)
  private RatingType type;

  private Long receiverId;

  @JoinColumn(name = "rater_id")
  @ManyToOne
  private User rater;
}

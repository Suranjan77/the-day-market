package com.thedaymarket.service.events;

import com.thedaymarket.domain.Rating;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RatingChangedEvent extends ApplicationEvent {
  private final Rating rating;

  public RatingChangedEvent(Object source, Rating rating) {
    super(source);
    this.rating = rating;
  }
}

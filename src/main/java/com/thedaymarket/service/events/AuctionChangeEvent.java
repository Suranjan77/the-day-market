package com.thedaymarket.service.events;

import com.thedaymarket.domain.Auction;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AuctionChangeEvent extends ApplicationEvent {
  private final Auction auction;
  public AuctionChangeEvent(Object source, Auction auction) {
    super(source);
    this.auction = auction;
  }
}

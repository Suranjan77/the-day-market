package com.thedaymarket.service;

import com.thedaymarket.controllers.request.BidRequest;
import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.Bid;
import com.thedaymarket.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Optional;

public interface BidService {
  Page<Bid> getBids(Auction auction, PageRequest pageRequest);
  Optional<Bid> getMaxBid(Auction auction);
  Bid addBids(Auction auction, BidRequest bidRequest);
  Page<Bid> getBidsFroUser(User user, PageRequest pageRequest);
  Bid getById(Long id);
}

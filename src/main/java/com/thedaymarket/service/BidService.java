package com.thedaymarket.service;

import com.thedaymarket.controllers.request.BidRequest;
import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface BidService {
  Page<Bid> getBids(Auction auction, PageRequest pageRequest);
  Optional<Bid> getMaxBid(Auction auction);

  Bid addBids(Auction auction, BidRequest bidRequest);
}

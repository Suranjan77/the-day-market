package com.thedaymarket.service.impl;

import com.thedaymarket.controllers.controller.LiveBiddingHandler;
import com.thedaymarket.controllers.request.BidRequest;
import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.Bid;
import com.thedaymarket.repository.BidRepository;
import com.thedaymarket.service.BidService;
import com.thedaymarket.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BidServiceImpl implements BidService {

  private final BidRepository bidRepository;
  private final UserService userService;
  private final LiveBiddingHandler biddingHandler;

  @Override
  public Page<Bid> getBids(Auction auction, PageRequest pageRequest) {
    return bidRepository.findAllByAuction(auction, pageRequest);
  }

  @Override
  public Optional<Bid> getMaxBid(Auction auction) {
    return bidRepository.findFirstByAuctionOrderByAmountDesc(auction);
  }

  @Override
  public Bid addBids(Auction auction, BidRequest bidRequest) {
    var bidder = userService.getUser(bidRequest.bidderId());
    var bid = new Bid();
    bid.setUser(bidder);
    bid.setAuction(auction);
    bid.setAmount(bidRequest.amount());
    var savedBid = bidRepository.save(bid);
    biddingHandler.notifyAll(auction.getId(), savedBid);
    return savedBid;
  }
}

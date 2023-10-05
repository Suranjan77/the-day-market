package com.thedaymarket.service.impl;

import com.thedaymarket.controllers.handlers.LiveBiddingHandler;
import com.thedaymarket.controllers.request.BidRequest;
import com.thedaymarket.controllers.response.BidResponse;
import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.Bid;
import com.thedaymarket.domain.User;
import com.thedaymarket.repository.BidRepository;
import com.thedaymarket.repository.UserPointsRepository;
import com.thedaymarket.service.BidService;
import com.thedaymarket.service.UserService;
import com.thedaymarket.utils.ExceptionUtils;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BidServiceImpl implements BidService {

  private final BidRepository bidRepository;
  private final UserService userService;
  private final UserPointsRepository userPointsRepository;
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

    validateBidAmount(auction, bidRequest, bidder);

    userPointsRepository.updateUserPointsAmount(
        bidder.getPoints().subtract(bidRequest.amount()), bidder.getUserPoints().getId());

    var bid = new Bid();
    bid.setUser(bidder);
    bid.setAuction(auction);
    bid.setAmount(bidRequest.amount());
    var savedBid = bidRepository.save(bid);
    biddingHandler.notifyAll(auction.getId(), BidResponse.of(savedBid));
    return savedBid;
  }

  private void validateBidAmount(Auction auction, BidRequest bidRequest, User bidder) {
    if (bidder.getPoints().compareTo(bidRequest.amount()) < 0) {
      throw ExceptionUtils.getBadRequestExceptionResponse("Not enough points");
    }

    var maxBid = getMaxBid(auction).map(Bid::getAmount).orElse(new BigDecimal("0.0"));
    switch (auction.getType()) {
      case English, Sealed -> {
        if (bidRequest.amount().compareTo(maxBid) <= 0) {
          throw ExceptionUtils.getBadRequestExceptionResponse(
              "Bid amount should be more than max bid.");
        }

        if (bidRequest.amount().compareTo(auction.getMinAskPrice()) < 0) {
          throw ExceptionUtils.getBadRequestExceptionResponse(
              "Bid amount must be more than or equal to minimum asking price.");
        }
      }
    }
  }
}

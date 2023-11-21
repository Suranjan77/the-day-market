package com.thedaymarket.service.impl;

import com.thedaymarket.controllers.handlers.LiveBiddingHandler;
import com.thedaymarket.controllers.request.BidRequest;
import com.thedaymarket.controllers.response.BidResponse;
import com.thedaymarket.domain.*;
import com.thedaymarket.repository.BidRepository;
import com.thedaymarket.repository.UserPointsRepository;
import com.thedaymarket.service.BidService;
import com.thedaymarket.service.PaymentService;
import com.thedaymarket.service.UserService;
import com.thedaymarket.service.schedule.DutchAuctionStateService;
import com.thedaymarket.utils.ExceptionUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BidServiceImpl implements BidService {

  private final BidRepository bidRepository;
  private final UserService userService;
  private final UserPointsRepository userPointsRepository;
  private final LiveBiddingHandler biddingHandler;
  private final DutchAuctionStateService dutchAuctionStateService;
  private final PaymentService paymentService;

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

    validateBid(auction, bidRequest, bidder);

    userPointsRepository.updateUserPointsAmount(
        bidder.getPoints().subtract(bidRequest.amount()), bidder.getUserPoints().getId());

    var bid = new Bid();
    bid.setBidder(bidder);
    bid.setAuction(auction);
    bid.setAmount(bidRequest.amount());
    var savedBid = bidRepository.save(bid);
    biddingHandler.notifyAll(auction.getId(), BidResponse.of(savedBid));

    paymentService.createTransaction(
        bid.getAmount(),
        bidder,
        auction.getSeller(),
        TransactionType.BID,
        PaymentMethod.POINTS_TRANSFER,
        savedBid.getId());

    if (auction.getType().equals(AuctionType.Dutch)) {
      dutchAuctionStateService.expireAuction(auction);
    }
    return savedBid;
  }

  @Override
  public Page<Bid> getBidsFroUser(User user, PageRequest pageRequest) {
    return bidRepository.findLatestBidsByUser(user.getId(), pageRequest);
  }

  @Override
  public Bid getById(Long id) {
    return bidRepository
        .findById(id)
        .orElseThrow(
            () -> ExceptionUtils.getNotFoundExceptionResponse("Bid not found for id: " + id));
  }

  private void validateBid(Auction auction, BidRequest bidRequest, User bidder) {

    if (bidder.getRole().equals(UserRole.SELLER)) {
      throw ExceptionUtils.getAuthenticationException("Sellers are not allowed to bid");
    }

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
      case Dutch -> {
        var alreadyBidForToday =
            bidRepository.existsByAuctionAndCreatedAt(auction, LocalDateTime.now());
        if (alreadyBidForToday) {
          throw new ExceptionUtils.BusinessException(
              HttpStatus.NOT_ACCEPTABLE, "Auction already closed for today");
        }
      }
    }
  }
}

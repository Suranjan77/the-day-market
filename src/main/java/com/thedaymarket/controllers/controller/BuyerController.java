package com.thedaymarket.controllers.controller;

import com.thedaymarket.controllers.response.MyBidResponse;
import com.thedaymarket.controllers.response.MyBidStatus;
import com.thedaymarket.controllers.response.PagedResponse;
import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.AuctionStatus;
import com.thedaymarket.domain.Rating;
import com.thedaymarket.domain.RatingType;
import com.thedaymarket.service.AuctionService;
import com.thedaymarket.service.BidService;
import com.thedaymarket.service.RatingService;
import com.thedaymarket.service.UserService;
import com.thedaymarket.service.events.AuctionChangeEvent;
import com.thedaymarket.utils.RESTUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/buyers")
public class BuyerController {
  private final UserService userService;
  private final BidService bidService;
  private final RatingService ratingService;
  private final AuctionService auctionService;
  private final ApplicationEventPublisher applicationEventPublisher;

  @GetMapping("{id}/my-bids")
  public PagedResponse<MyBidResponse> getLatestBids(
      @PathVariable("id") Long buyerId,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size) {
    var buyer = userService.getUser(buyerId);
    var pageRequest = RESTUtils.getPageRequest(page, size);
    var bids = bidService.getBidsFroUser(buyer, pageRequest);

    return RESTUtils.getPagedResponse(
        bids.map(
            bid -> {
              var auction = bid.getAuction();
              var rating =
                  ratingService
                      .getRatingByRater(RatingType.BID, buyer, bid.getId())
                      .map(Rating::getStars)
                      .orElse(0.0f);

              var bidWon = auction.getWinningBid() != null;

              var amIWinner = bidWon && auction.getWinningBid().getBidder().getId().equals(buyerId);

              MyBidStatus bidStatus;

              if (amIWinner && !auction.getStatus().equals(AuctionStatus.SOLD)) {
                bidStatus = MyBidStatus.WON;
              } else if (List.of(AuctionStatus.CLOSED, AuctionStatus.UNSOLD)
                  .contains(auction.getStatus())) {
                bidStatus = MyBidStatus.LOST;
              } else if (auction.getStatus().equals(AuctionStatus.PUBLISHED)) {
                bidStatus = MyBidStatus.LIVE;
              } else {
                bidStatus = MyBidStatus.CLOSED;
              }

              return new MyBidResponse(
                  bid.getId(),
                  auction.getImageName(),
                  auction.getId(),
                  auction.getTitle(),
                  auction.getDescription(),
                  bid.getAmount(),
                  bidWon ? auction.getWinningBid().getAmount() : null,
                  rating,
                  bidStatus,
                  amIWinner);
            }));
  }

  @PostMapping("{id}/my-bid-action")
  public BidActionResponse performActionOnBid(
      @PathVariable("id") Long buyerId, @RequestBody BidActionRequest request) {
    var buyer = userService.getUser(buyerId);
    var bid = bidService.getById(request.bidId());
    if (request.isPurchase()) {
      auctionService.confirmPurchase(buyer, bid);
    } else {
      auctionService.confirmRejection(buyer, bid);
    }

    applicationEventPublisher.publishEvent(new AuctionChangeEvent(this, bid.getAuction()));

    return new BidActionResponse(bid.getId(), request.isPurchase);
  }

  public record BidActionRequest(Long bidId, boolean isPurchase) {}

  public record BidActionResponse(Long bidId, boolean purchased) {}
}

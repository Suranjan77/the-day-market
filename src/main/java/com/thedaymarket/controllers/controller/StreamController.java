package com.thedaymarket.controllers.controller;

import com.thedaymarket.controllers.handlers.DutchAuctionStateHandler;
import com.thedaymarket.controllers.handlers.LiveBiddingHandler;
import com.thedaymarket.controllers.response.BidResponse;
import com.thedaymarket.controllers.response.DutchAuctionStateResponse;
import com.thedaymarket.controllers.response.StreamResponse;
import com.thedaymarket.service.AuctionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/stream")
public class StreamController {

  private final AuctionService auctionService;
  private final LiveBiddingHandler liveBiddingHandler;
  private final DutchAuctionStateHandler dutchAuctionStateHandler;

  @GetMapping("auctions/{id}/bids")
  public DeferredResult<StreamResponse<BidResponse>> streamBids(
      @PathVariable("id") Long auctionId) {
    auctionService.getAuction(auctionId);
    var client = new DeferredResult<StreamResponse<BidResponse>>(30_000L);
    liveBiddingHandler.registerClient(auctionId, client);
    return client;
  }

  @RequestMapping("auctions/{id}/state")
  public DeferredResult<StreamResponse<DutchAuctionStateResponse>> getAuctionStateStream(
      @PathVariable("id") Long auctionId) {
    auctionService.getAuction(auctionId);
    var client = new DeferredResult<StreamResponse<DutchAuctionStateResponse>>(30_000L);
    dutchAuctionStateHandler.registerClient(auctionId, client);
    return client;
  }
}

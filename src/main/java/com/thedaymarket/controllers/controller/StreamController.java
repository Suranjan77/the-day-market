package com.thedaymarket.controllers.controller;

import com.thedaymarket.controllers.response.StreamBidResponse;
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

  @GetMapping("auctions/{id}/bids")
  public DeferredResult<StreamBidResponse> streamBids(@PathVariable("id") Long auctionId) {
    auctionService.getAuction(auctionId);
    var client = new DeferredResult<StreamBidResponse>(30_000L); // 30 seconds timeout
    liveBiddingHandler.registerClient(auctionId, client);
    return client;
  }
}

package com.thedaymarket.controllers.controller;

import com.thedaymarket.controllers.request.BidRequest;
import com.thedaymarket.controllers.response.AuctionShortResponse;
import com.thedaymarket.controllers.response.PagedResponse;
import com.thedaymarket.domain.Auction;
import com.thedaymarket.service.AuctionService;
import com.thedaymarket.utils.JsonUtils;
import com.thedaymarket.utils.RESTUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/auctions")
public class AuctionController {

  private final AuctionService auctionService;

  @GetMapping
  public PagedResponse<List<Auction>> searchAuction(
      @RequestParam("query") String searchQuery,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size) {
    return null;
  }

  @GetMapping("{id}")
  public Auction getAuction(@PathVariable("id") Long auctionId) {
    return null;
  }

  @GetMapping("/today")
  public PagedResponse<List<AuctionShortResponse>> getTodayAuctions(
      @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
    var pageRequest = RESTUtils.getPageRequest(page, size);
    var auctionsPage = auctionService.getTodayAuctions(pageRequest);
    var auctionPageResponse = auctionsPage.map(AuctionShortResponse::fromAuction);
    return RESTUtils.getPagedResponse(auctionPageResponse);
  }

  // todo: Long polling =>  .GET("/auctions/{auctionId}/bids/stream", biddingHandler::streamBids)

  @PostMapping("{id}/bids")
  public void placeBid(
      @PathVariable("id") Long auctionId,
      @RequestParam("userId") Long bidderId,
      @RequestBody @Valid BidRequest bidRequest) {}
}

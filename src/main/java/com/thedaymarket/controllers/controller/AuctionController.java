package com.thedaymarket.controllers.controller;

import com.thedaymarket.controllers.request.BidRequest;
import com.thedaymarket.controllers.response.*;
import com.thedaymarket.domain.Auction;
import com.thedaymarket.domain.AuctionType;
import com.thedaymarket.service.AuctionService;
import com.thedaymarket.service.BidService;
import com.thedaymarket.utils.RESTUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/auctions")
public class AuctionController {

  private final AuctionService auctionService;
  private final BidService bidService;

  @GetMapping
  // todo: Search auction by the query
  public PagedResponse<List<Auction>> searchAuction(
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size,
      @RequestParam(value = "query", required = false) String query,
      @RequestParam(value = "filters", required = false) String filters,
      @RequestParam(value = "sort", required = false) String sort) {

    return null;
  }

  @GetMapping("{id}/bids")
  public PagedResponseWithMax<BidResponse> getBids(
      @PathVariable("id") Long auctionId,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size) {
    var auction = auctionService.getAuction(auctionId);
    var hideAmount = auction.getType().equals(AuctionType.Sealed);

    var bids =
        bidService.getBids(
            auction,
            RESTUtils.getPageRequest(page, size)
                .withSort(Sort.Direction.DESC, "createdAt", "amount"));

    BidResponse maxBid = null;
    if (!hideAmount) {
      maxBid = bidService.getMaxBid(auction).map(b -> BidResponse.of(b, false)).orElse(null);
    }

    return RESTUtils.getPagedResponseWithMax(maxBid, bids.map(b -> BidResponse.of(b, hideAmount)));
  }

  @GetMapping("{id}")
  public AuctionResponse getAuction(@PathVariable("id") Long auctionId) {
    return AuctionResponse.fromAuction(auctionService.getAuction(auctionId));
  }

  @GetMapping("/today")
  public PagedResponse<AuctionShortResponse> getTodayAuctions(
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size,
      @RequestParam(value = "filters", required = false) String filters,
      @RequestParam(value = "sort", required = false) String sort) {
    var pageRequest = RESTUtils.getPageRequest(page, size, sort);
    var auctionsPage = auctionService.getTodayAuctions(pageRequest, filters);
    Page<AuctionShortResponse> auctionPageResponse =
        auctionsPage.map(AuctionShortResponse::fromAuction);
    return RESTUtils.getPagedResponse(auctionPageResponse);
  }

  @PostMapping("{id}/bids")
  public BidResponse placeBid(
      @PathVariable("id") Long auctionId, @RequestBody @Valid BidRequest bidRequest) {
    var auction = auctionService.getAuction(auctionId);
    var bid = bidService.addBids(auction, bidRequest);
    return BidResponse.of(bid);
  }

  @DeleteMapping("{id}")
  public void deleteAuction(@PathVariable("id") Long auctionId) {
    auctionService.deleteAuction(auctionId);
  }

  @RequestMapping("{id}/state")
  public DutchAuctionStateResponse getAuctionState(@PathVariable("id") Long auctionId) {
    return DutchAuctionStateResponse.of(auctionService.getDutchAuctionState(auctionId));
  }
}

package com.thedaymarket.controllers.controller;

import com.thedaymarket.controllers.request.AuctionDetailsRequest;
import com.thedaymarket.controllers.request.CreateAuctionRequest;
import com.thedaymarket.controllers.response.AuctionResponse;
import com.thedaymarket.controllers.response.AuctionShortResponse;
import com.thedaymarket.controllers.response.PagedResponse;
import com.thedaymarket.domain.Auction;
import com.thedaymarket.service.AuctionService;
import com.thedaymarket.service.UserService;
import com.thedaymarket.utils.ExceptionUtils;
import com.thedaymarket.utils.RESTUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.ServerResponse;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/sellers")
public class SellerController {
  private final AuctionService auctionService;
  private final UserService userService;

  @GetMapping("{id}/auctions")
  public PagedResponse<AuctionShortResponse> getAuctions(
      @PathVariable("id") Long sellerId,
      @RequestParam(value = "includeDrafts", required = false, defaultValue = "false")
          Boolean includeDrafts,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size) {
    var seller = userService.getUser(sellerId);
    var pageRequest = RESTUtils.getPageRequest(page, size);
    var pagedAuctions = auctionService.getAuctionsBySeller(seller, includeDrafts, pageRequest);
    return RESTUtils.getPagedResponse(pagedAuctions.map(AuctionShortResponse::fromAuction));
  }

  @GetMapping("{id}/auctions/today")
  public PagedResponse<AuctionShortResponse> getTodayAuctions(
      @PathVariable("id") Long sellerId,
      @RequestParam(value = "includeDrafts", required = false, defaultValue = "false")
          Boolean includeDrafts,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size) {
    var seller = userService.getUser(sellerId);
    var pageRequest = RESTUtils.getPageRequest(page, size);
    var pagedAuctions = auctionService.getTodayAuctionsBySeller(seller, includeDrafts, pageRequest);
    var auctionsResponse = pagedAuctions.map(AuctionShortResponse::fromAuction);
    return RESTUtils.getPagedResponse(auctionsResponse);
  }

  @PostMapping("{id}/auctions")
  public AuctionResponse createAuction(
      @PathVariable("id") Long sellerId,
      @RequestBody @Valid CreateAuctionRequest createAuctionRequest) {
    var seller = userService.getUser(sellerId);
    var auction = auctionService.createAuction(seller, createAuctionRequest);
    return AuctionResponse.fromAuction(auction);
  }

  @PostMapping("{id}/auctions/{auctionId}/image")
  public AuctionResponse uploadAuctionImage(
      @PathVariable("id") Long sellerId,
      @PathVariable("auctionId") Long auctionId,
      @RequestParam("file") MultipartFile image) {
    var seller = userService.getUser(sellerId);

    if (image != null) {
      var auction = auctionService.uploadAuctionImage(seller, auctionId, image);
      return AuctionResponse.fromAuction(auction);
    }

    throw ExceptionUtils.getBadRequestExceptionResponse("No file was uploaded.");
  }

  @PostMapping("{id}/auctions/{auctionId}")
  public AuctionResponse updateAuctionDetails(
      @PathVariable("id") Long sellerId,
      @PathVariable("auctionId") Long auctionId,
      @RequestBody @Valid AuctionDetailsRequest auctionDetails) {
    var seller = userService.getUser(sellerId);

    var auction = auctionService.updateAuctionDetails(seller, auctionId, auctionDetails);
    return AuctionResponse.fromAuction(auction);
  }

  @DeleteMapping("{id}/auctions/{auctionId}")
  public ResponseEntity<?> deleteAuction(
      @PathVariable("id") Long sellerId, @PathVariable("auctionId") Long auctionId) {

    auctionService.deleteAuction(auctionId);

    return ResponseEntity.ok().build();
  }
}

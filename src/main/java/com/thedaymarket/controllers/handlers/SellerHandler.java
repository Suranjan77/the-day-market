package com.thedaymarket.controllers.handlers;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class SellerHandler {
  public ServerResponse getAuctions(ServerRequest req) {
    //      @PathVariable("sellerId") Long sellerId
    return ServerResponse.ok().build();
  }

  public ServerResponse createAuction(ServerRequest req) {
    //      @PathVariable("sellerId") Long sellerId, @RequestBody CreateAuctionRequest
    // auctionRequest
    return ServerResponse.ok().build();
  }

  public ServerResponse uploadPicture(ServerRequest req) {
    //      @PathVariable("sellerId") Long sellerId,
    //      @PathVariable("auctionId") Long auctionId,
    //      @RequestParam("file") MultipartFile image

    return ServerResponse.ok().build();
  }

  public ServerResponse updateAuctionDetails(ServerRequest req) {
    //      @PathVariable("sellerId") Long sellerId,
    //      @PathVariable("auctionId") Long auctionId,
    //      @RequestBody AuctionDetailsRequest auctionDetailsRequest
    return ServerResponse.ok().build();
  }

  public ServerResponse deleteAuction(ServerRequest req) {
    //      @PathVariable("sellerId") Long sellerId, @PathVariable("auctionId") Long auctionId
    return ServerResponse.ok().build();
  }
}

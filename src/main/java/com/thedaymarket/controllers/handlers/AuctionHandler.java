package com.thedaymarket.controllers.handlers;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class AuctionHandler {
  public ServerResponse searchAuction(ServerRequest req) {
    //    String query,
    //    String filter,
    //    String sorted,
    //    @RequestParam("page") Integer page,
    //    @RequestParam("size") Integer size
    return ServerResponse.ok().build();
  }

  public ServerResponse getAuction(ServerRequest req) {
    //  @PathVariable("auctionId") Long auctionId{
    return ServerResponse.ok().build();
  }
}

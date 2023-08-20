package com.thedaymarket.controllers.handlers;

import com.thedaymarket.service.AuctionService;
import com.thedaymarket.utils.RESTUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@AllArgsConstructor
@Component
public class AuctionHandler {

  private final AuctionService auctionService;

  public ServerResponse searchAuction(ServerRequest req) {
    return ServerResponse.ok().build();
  }

  public ServerResponse getAuction(ServerRequest req) {
    return ServerResponse.ok().build();
  }

  public ServerResponse getTodayAuctions(ServerRequest req) {
    var pageRequest = RESTUtils.getPageRequest(req);
    var auctionsPage = auctionService.getTodayAuctions(pageRequest);
    return ServerResponse.ok().body(RESTUtils.getPagedResponse(auctionsPage));
  }
}

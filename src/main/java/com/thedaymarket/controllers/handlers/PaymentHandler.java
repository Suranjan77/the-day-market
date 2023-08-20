package com.thedaymarket.controllers.handlers;

import com.thedaymarket.controllers.request.BuyPointsRequest;
import com.thedaymarket.controllers.request.SellPointsRequest;
import com.thedaymarket.utils.RESTUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class PaymentHandler {

  public ServerResponse buyPoints(ServerRequest req) {
    var buyPointsReq = RESTUtils.parseRequest(req, BuyPointsRequest.class);
    Long userId =
        req.param("userId")
            .map(Long::parseLong)
            .orElseThrow(() -> new ServerWebInputException("Missing request parameter [userId]"));
    return ServerResponse.ok().build();
  }

  public ServerResponse sellPoints(ServerRequest req) {
    var sellPointsReq = RESTUtils.parseRequest(req, SellPointsRequest.class);
    Long userId =
        req.param("userId")
            .map(Long::parseLong)
            .orElseThrow(() -> new ServerWebInputException("Missing request parameter [userId]"));
    return ServerResponse.ok().build();
  }
}

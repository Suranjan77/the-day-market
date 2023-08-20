package com.thedaymarket.controllers.handlers;

import com.thedaymarket.controllers.request.AuctionDetailsRequest;
import com.thedaymarket.controllers.request.CreateAuctionRequest;
import com.thedaymarket.service.AuctionService;
import com.thedaymarket.service.UserService;
import com.thedaymarket.utils.ExceptionUtils;
import com.thedaymarket.utils.RESTUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Validator;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@AllArgsConstructor
@Component
public class SellerHandler {

  public static final String REQUIRED_PARAMETER_SELLER_ID_MESSAGE =
      "Required parameter sellerId [id]";

  private final AuctionService auctionService;
  private final UserService userService;
  private final Validator validator;

  public ServerResponse getAuctions(ServerRequest req) {
    var sellerId = RESTUtils.getPathVariable(req, "id");

    var includeDrafts =
        req.param("includeDrafts")
            .filter(Boolean::parseBoolean)
            .map(Boolean::parseBoolean)
            .orElse(false);

    var seller = userService.getUser(Long.parseLong(sellerId));
    var pageRequest = RESTUtils.getPageRequest(req);
    var pagedAuctions = auctionService.getAuctionsBySeller(seller, includeDrafts, pageRequest);
    return ServerResponse.ok().body(RESTUtils.getPagedResponse(pagedAuctions));
  }

  public ServerResponse getTodayAuction(ServerRequest req) {
    var sellerId = RESTUtils.getPathVariable(req, "id");
    var includeDrafts =
        req.param("includeDrafts")
            .filter(Boolean::parseBoolean)
            .map(Boolean::parseBoolean)
            .orElse(false);
    var seller = userService.getUser(Long.parseLong(sellerId));
    var pageRequest = RESTUtils.getPageRequest(req);
    var pagedAuctions = auctionService.getTodayAuctionsBySeller(seller, includeDrafts, pageRequest);
    return ServerResponse.ok().body(RESTUtils.getPagedResponse(pagedAuctions));
  }

  public ServerResponse createAuction(ServerRequest req) {
    var sellerId = RESTUtils.getPathVariable(req, "id");
    var createAuctionRequest =
        RESTUtils.parseRequestBody(req, CreateAuctionRequest.class, validator);
    var seller = userService.getUser(Long.parseLong(sellerId));
    var auction = auctionService.createAuction(seller, createAuctionRequest);
    return ServerResponse.ok().body(auction);
  }

  public ServerResponse uploadPicture(ServerRequest req) {
    //      @PathVariable("sellerId") Long sellerId,
    //      @PathVariable("auctionId") Long auctionId,
    //      @RequestParam("file") MultipartFile image

    return ServerResponse.ok().build();
  }

  public ServerResponse updateAuctionDetails(ServerRequest req) {
    var sellerId = RESTUtils.getPathVariable(req, "id");
    var seller = userService.getUser(Long.parseLong(sellerId));

    var auctionId = RESTUtils.getPathVariable(req, "auctionId");

    var auctionDetails = RESTUtils.parseRequestBody(req, AuctionDetailsRequest.class, validator);

    auctionService.updateAuctionDetails(seller, Long.parseLong(auctionId), auctionDetails);

    return ServerResponse.ok().build();
  }

  public ServerResponse deleteAuction(ServerRequest req) {
    var auctionId = RESTUtils.getPathVariable(req, "auctionId");

    auctionService.deleteAuction(Long.parseLong(auctionId));

    return ServerResponse.ok().build();
  }
}

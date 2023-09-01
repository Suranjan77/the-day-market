package com.thedaymarket.controllers.handlers;

import com.thedaymarket.controllers.request.AuctionDetailsRequest;
import com.thedaymarket.controllers.request.CreateAuctionRequest;
import com.thedaymarket.service.AuctionService;
import com.thedaymarket.service.UserService;
import com.thedaymarket.utils.RESTUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Slf4j
@AllArgsConstructor
@Component
public class SellerHandler {

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

  public ServerResponse uploadAuctionImage(ServerRequest req) {
    var sellerId = Long.parseLong(RESTUtils.getPathVariable(req, "id"));
    var seller = userService.getUser(sellerId);
    var auctionId = Long.parseLong(RESTUtils.getPathVariable(req, "auctionId"));

    var servletRequest = req.servletRequest();

    if (servletRequest instanceof StandardMultipartHttpServletRequest multiPartRequest) {
      MultipartFile image = multiPartRequest.getFile("file");
      if (image != null) {
        var auction = auctionService.uploadAuctionImage(seller, auctionId, image);
        return ServerResponse.ok().body(auction);
      }
    }

    return ServerResponse.badRequest().body("No file was uploaded");
  }

  public ServerResponse updateAuctionDetails(ServerRequest req) {
    var sellerId = RESTUtils.getPathVariable(req, "id");
    var seller = userService.getUser(Long.parseLong(sellerId));

    var auctionId = RESTUtils.getPathVariable(req, "auctionId");

    var auctionDetails = RESTUtils.parseRequestBody(req, AuctionDetailsRequest.class, validator);

    var updatedAuction =
        auctionService.updateAuctionDetails(seller, Long.parseLong(auctionId), auctionDetails);

    return ServerResponse.ok().body(updatedAuction);
  }

  public ServerResponse deleteAuction(ServerRequest req) {
    var auctionId = RESTUtils.getPathVariable(req, "auctionId");

    auctionService.deleteAuction(Long.parseLong(auctionId));

    return ServerResponse.ok().build();
  }
}

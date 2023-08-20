package com.thedaymarket.controllers;

import static org.springframework.web.servlet.function.RequestPredicates.accept;
import static org.springframework.web.servlet.function.RouterFunctions.route;

import com.thedaymarket.controllers.handlers.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.*;

@Slf4j
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = "com.thedaymarket.controllers.handlers")
public class RoutingConfiguration {
  private static final RequestPredicate ACCEPT_JSON = accept(MediaType.APPLICATION_JSON);

  @Autowired private AuctionHandler auctionHandler;
  @Autowired private BiddingHandler biddingHandler;
  @Autowired private CategoriesHandler categoriesHandler;
  @Autowired private PaymentHandler paymentHandler;
  @Autowired private SellerHandler sellerHandler;
  @Autowired private HealthCheckHandler healthCheckHandler;

  @Bean
  public RouterFunction<ServerResponse> apiRouter() {
    return route().path("/api/v1", this::apiRoutes).filter(this::logRequest).build();
  }

  public RouterFunction<ServerResponse> apiRoutes() {
    return route()
        .add(auctionRouter())
        .add(bidRouter())
        .add(categoriesRouter())
        .add(paymentRouter())
        .add(sellerRouter())
        .add(healthCheckRouter())
        .build();
  }

  public RouterFunction<ServerResponse> auctionRouter() {
    return route()
        .GET("/auctions/{id}", auctionHandler::getAuction)
        .GET("/auctions", auctionHandler::searchAuction)
        .build();
  }

  public RouterFunction<ServerResponse> bidRouter() {
    return route()
        .GET("/auctions/{auctionId}/bids/stream", biddingHandler::streamBids)
        .POST("/auctions/{auctionId}/bids", ACCEPT_JSON, biddingHandler::addBid)
        .build();
  }

  public RouterFunction<ServerResponse> categoriesRouter() {
    return route()
        .GET("/categories", categoriesHandler::createCategory)
        .POST("/categories/{id}/children", ACCEPT_JSON, categoriesHandler::createSubCategory)
        .GET("/categories", categoriesHandler::getCategories)
        .GET("/categories/{id}", categoriesHandler::getCategory)
        .GET("/categories/{id}/children", categoriesHandler::getChildrenCategories)
        .DELETE("/categories/{id}", categoriesHandler::deleteCategory)
        .build();
  }

  public RouterFunction<ServerResponse> paymentRouter() {
    return route()
        .POST("/payments/buy", ACCEPT_JSON, paymentHandler::buyPoints)
        .POST("/payments/sell", ACCEPT_JSON, paymentHandler::sellPoints)
        .build();
  }

  public RouterFunction<ServerResponse> sellerRouter() {
    return route()
        .GET("/sellers/{id}/auctions", sellerHandler::getAuctions)
        .POST("/sellers/{id}/auctions", ACCEPT_JSON, sellerHandler::createAuction)
        .POST("/sellers/{id}/auctions/{auctionId}/image", ACCEPT_JSON, sellerHandler::uploadPicture)
        .PATCH(
            "/sellers/{id}/auctions/{auctionId}", ACCEPT_JSON, sellerHandler::updateAuctionDetails)
        .DELETE("/sellers/{id}/auctions/{auctionId}", sellerHandler::deleteAuction)
        .build();
  }

  public RouterFunction<ServerResponse> healthCheckRouter() {
    return route().GET("/healthcheck", healthCheckHandler::healthCheck).build();
  }

  public ServerResponse logRequest(ServerRequest req, HandlerFunction<ServerResponse> next)
      throws Exception {
    log.info("Invoked {}:{}", req.method(), req.path());
    return next.handle(req);
  }
}

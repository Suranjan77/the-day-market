package com.thedaymarket.controllers;

import static org.springframework.web.servlet.function.RequestPredicates.accept;
import static org.springframework.web.servlet.function.RouterFunctions.route;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thedaymarket.controllers.handlers.*;
import com.thedaymarket.utils.ExceptionUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.function.*;

import java.util.List;

@Slf4j
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@ComponentScan(basePackages = "com.thedaymarket.controllers.handlers")
public class RoutingConfiguration {
  private static final RequestPredicate ACCEPT_JSON = accept(MediaType.APPLICATION_JSON);

  private final AuctionHandler auctionHandler;
  private final BiddingHandler biddingHandler;
  private final CategoriesHandler categoriesHandler;
  private final PaymentHandler paymentHandler;
  private final SellerHandler sellerHandler;
  private final HealthCheckHandler healthCheckHandler;
  private final AuthHandler authHandler;

  @Bean
  public RouterFunction<ServerResponse> apiRouter() {
    return route()
        .path("/api/v1", this::apiRoutes)
        .filter(this::logRequest)
        .onError(
            ExceptionUtils.BusinessException.class,
            (e, req) ->
                EntityResponse.fromObject(new ApiError(e.getMessage(), null))
                    .status(((ExceptionUtils.BusinessException) e).getStatusCode())
                    .build())
        .onError(
            MethodArgumentNotValidException.class,
            (e, req) -> {
              var errors =
                  ((MethodArgumentNotValidException) e)
                      .getBindingResult().getFieldErrors().stream()
                          .map(FieldError::getDefaultMessage)
                          .toList();

              return EntityResponse.fromObject(new ApiError(null, errors))
                  .status(HttpStatus.UNPROCESSABLE_ENTITY)
                  .build();
            })
        .build();
  }

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record ApiError(String error, List<String> errors) {}

  public RouterFunction<ServerResponse> apiRoutes() {
    return route()
        .add(auctionRouter())
        .add(bidRouter())
        .add(categoriesRouter())
        .add(paymentRouter())
        .add(sellerRouter())
        .add(healthCheckRouter())
        .add(authRouter())
        .build();
  }

  public RouterFunction<ServerResponse> authRouter() {
    return route()
        .POST("/auth/login", authHandler::login)
        .POST("/auth/register", authHandler::register)
        .build();
  }

  public RouterFunction<ServerResponse> auctionRouter() {
    return route()
        .GET("/auctions/{id}", auctionHandler::getAuction)
        .GET("/auctions/today", auctionHandler::getTodayAuctions)
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
        .GET("/sellers/{id}/auctions/today", sellerHandler::getTodayAuction)
        .POST("/sellers/{id}/auctions", ACCEPT_JSON, sellerHandler::createAuction)
        .POST(
            "/sellers/{id}/auctions/{auctionId}/image",
            accept(MediaType.MULTIPART_FORM_DATA),
            sellerHandler::uploadAuctionImage)
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

package com.thedaymarket.controllers.response;

import com.thedaymarket.domain.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AuctionResponse(
    Long id,
    String title,
    String description,
    BigDecimal minAskPrice,
    Double decrementFactor,
    Long decrementSeconds,
    AuctionType type,
    Long itemCount,
    AuctionStatus status,
    LocalDate scheduledDate,
    String imageName,
    CategoriesResponse categoriesResponse) {

  public static AuctionResponse fromAuction(Auction auction) {
    return new AuctionResponse(
        auction.getId(),
        auction.getTitle(),
        auction.getDescription(),
        auction.getMinAskPrice(),
        auction.getDecrementFactor(),
        auction.getDecrementSeconds(),
        auction.getType(),
        auction.getItemCount(),
        auction.getStatus(),
        auction.getScheduledDate(),
        auction.getImageName(),
        CategoriesResponse.of(auction.getCategory()));
  }
}

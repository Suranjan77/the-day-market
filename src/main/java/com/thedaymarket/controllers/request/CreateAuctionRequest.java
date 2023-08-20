package com.thedaymarket.controllers.request;

import com.thedaymarket.domain.AuctionType;

public record CreateAuctionRequest(
    Long categoryId,
    String title,
    AuctionType type,
    String description) {}

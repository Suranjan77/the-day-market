package com.thedaymarket.controllers.request;

import com.thedaymarket.domain.AuctionType;
import jakarta.validation.constraints.NotNull;

public record CreateAuctionRequest(
    @NotNull String category,
    @NotNull String title,
    @NotNull AuctionType type,
    @NotNull String description,
    @NotNull Boolean isDraft) {}

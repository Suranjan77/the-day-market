package com.thedaymarket.controllers.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record AuctionDetailsRequest(
    LocalDate scheduledDate,
    LocalTime scheduledTime,
    Long decrementSeconds,
    BigDecimal decrementFactor,
    BigDecimal startingPrice,
    Long itemCount,
    Long categoryId,
    boolean publish) {}

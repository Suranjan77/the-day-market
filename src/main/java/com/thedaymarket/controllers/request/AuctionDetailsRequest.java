package com.thedaymarket.controllers.request;

import com.thedaymarket.controllers.request.validation.MarketScheduleTimeConstraint;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record AuctionDetailsRequest(
    @FutureOrPresent(message = "Scheduled date should be in future or today")
        LocalDate scheduledDate,
    @MarketScheduleTimeConstraint LocalTime scheduledTime,
    @Min(value = 60, message = "Decrement interval must be more than or equal to a minute")
        @Max(value = 7200, message = "Decrement interval must be less than or equal to 2 hours")
        Long decrementSeconds,
    @Max(value = 100, message = "Decrement factor must be in the range 0-100")
        Double decrementFactor,
    @Positive(message = "Ask price must be greater than 0") BigDecimal minAskPrice,
    Long itemCount,
    Long categoryId,
    boolean publish) {}

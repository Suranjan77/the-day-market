package com.thedaymarket.controllers.request;

import com.thedaymarket.domain.RatingType;

public record RatingRequest(Long raterId, Long receiverId, Integer stars, RatingType type) {}

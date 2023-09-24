package com.thedaymarket.controllers.response;

import java.util.List;

public record BidsWithMaxResponse(BidResponse max, BidResponse secondMax, List<BidResponse> bids) {}

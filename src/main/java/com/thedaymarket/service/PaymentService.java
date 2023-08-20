package com.thedaymarket.service;

import com.thedaymarket.controllers.request.BuyPointsRequest;
import com.thedaymarket.controllers.request.SellPointsRequest;
import com.thedaymarket.domain.Transaction;

public interface PaymentService {
  Transaction buyPoints(BuyPointsRequest buyPoints, Long userId);

  Transaction sellPoints(SellPointsRequest buyPoints, Long userId);
}

package com.thedaymarket.service;

import com.thedaymarket.controllers.request.BuyPointsRequest;
import com.thedaymarket.controllers.request.SellPointsRequest;
import com.thedaymarket.domain.PaymentMethod;
import com.thedaymarket.domain.Transaction;
import com.thedaymarket.domain.TransactionType;
import com.thedaymarket.domain.User;
import java.math.BigDecimal;

public interface PaymentService {
  Transaction buyPoints(BuyPointsRequest buyPoints, Long userId);

  Transaction sellPoints(SellPointsRequest buyPoints, Long userId);

  Transaction buyAuction(BigDecimal amount, User buyer, User seller, Long bidId);

  Transaction refundBid(BigDecimal amount, User buyer, User seller, Long bidId);

  Transaction refundTransaction(Transaction transaction);

  Transaction createTransaction(
      BigDecimal amount,
      User buyer,
      User seller,
      TransactionType type,
      PaymentMethod paymentMethod,
      Long orderId);
}

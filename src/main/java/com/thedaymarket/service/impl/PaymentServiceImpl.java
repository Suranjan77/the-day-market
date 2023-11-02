package com.thedaymarket.service.impl;

import com.thedaymarket.controllers.request.BuyPointsRequest;
import com.thedaymarket.controllers.request.SellPointsRequest;
import com.thedaymarket.domain.*;
import com.thedaymarket.repository.TransactionRepository;
import com.thedaymarket.repository.UserPointsRepository;
import com.thedaymarket.service.PaymentService;
import com.thedaymarket.service.UserService;
import com.thedaymarket.utils.ExceptionUtils;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.stereotype.Service;

@Transactional
@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

  private final PaymentSimulator paymentSimulator;
  private final UserService userService;
  private final TransactionRepository transactionRepository;
  private final UserPointsRepository userPointsRepository;

  private static final Long POINTS_ORDER_ID = 0L;

  @Override
  public Transaction buyPoints(BuyPointsRequest buyPoints, Long userId) {
    var creditCardDetails = buyPoints.creditCardDetails();
    var buyer = userService.getUser(userId);
    var transactionResponse =
        paymentSimulator.createCreditCardTransaction(creditCardDetails, buyPoints.amount());
    if (transactionResponse.success()) {
      var transaction =
          createTransaction(
              buyPoints.amount(),
              buyer,
              userService.getSystemUser(),
              TransactionType.PURCHASE_POINTS,
              transactionResponse.paymentMethod(),
              POINTS_ORDER_ID);

      updateUserPoints(buyPoints.amount(), buyer, true);

      return transaction;
    } else {
      throw new WebServerException("Transaction failed for user: " + userId, new Throwable());
    }
  }

  @Override
  public Transaction sellPoints(SellPointsRequest sellPoints, Long userId) {
    var creditCardDetails = sellPoints.bankAccountDetails();
    var seller = userService.getUser(userId);
    if (seller.getPoints().compareTo(sellPoints.points()) < 0) {
      throw ExceptionUtils.getNotFoundExceptionResponse("Not enough points");
    }
    var transactionResponse =
        paymentSimulator.createBankAccountTransaction(creditCardDetails, sellPoints.points());
    if (transactionResponse.success()) {
      var transaction =
          createTransaction(
              sellPoints.points(),
              userService.getSystemUser(),
              seller,
              TransactionType.SELL_POINTS,
              transactionResponse.paymentMethod(),
              POINTS_ORDER_ID);

      updateUserPoints(sellPoints.points(), seller, false);

      return transaction;
    } else {
      throw new WebServerException("Transaction failed for user: " + userId, new Throwable());
    }
  }

  @Override
  public Transaction buyAuction(BigDecimal amount, User buyer, User seller, Long bidId) {
    return createTransaction(
        amount,
        buyer,
        seller,
        TransactionType.PURCHASE_AUCTION,
        PaymentMethod.POINTS_TRANSFER,
        bidId);
  }

  @Override
  public Transaction refundBid(BigDecimal amount, User buyer, User seller, Long bidId) {
    return createTransaction(
        amount, buyer, seller, TransactionType.REFUND, PaymentMethod.POINTS_TRANSFER, bidId);
  }

  @Override
  public Transaction refundTransaction(Transaction transaction) {
    return createTransaction(
        transaction.getAmount(),
        transaction.getBuyer(),
        transaction.getSeller(),
        TransactionType.REFUND,
        PaymentMethod.POINTS_TRANSFER,
        transaction.getOrderId());
  }

  private void updateUserPoints(BigDecimal amount, User buyer, final boolean isAddition) {
    var userPoints =
        userPointsRepository
            .findByBelongsTo(buyer)
            .orElseGet(
                () -> {
                  var points = new UserPoints();
                  points.setBelongsTo(buyer);
                  points.setCount(new BigDecimal("0"));
                  return points;
                });

    var finalPoints =
        isAddition ? userPoints.getCount().add(amount) : userPoints.getCount().subtract(amount);

    userPoints.setCount(finalPoints);

    userPointsRepository.save(userPoints);
  }

  public Transaction createTransaction(
      BigDecimal amount,
      User buyer,
      User seller,
      TransactionType type,
      PaymentMethod paymentMethod,
      Long orderId) {
    var transaction = new Transaction();
    transaction.setAmount(amount);
    transaction.setType(type);
    transaction.setBuyer(buyer);
    transaction.setSeller(seller);
    transaction.setOrderId(orderId);
    transaction.setPaymentMethod(paymentMethod);
    return transactionRepository.save(transaction);
  }
}

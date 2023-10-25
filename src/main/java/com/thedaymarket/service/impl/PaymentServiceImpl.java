package com.thedaymarket.service.impl;

import com.thedaymarket.controllers.request.BuyPointsRequest;
import com.thedaymarket.controllers.request.SellPointsRequest;
import com.thedaymarket.domain.Transaction;
import com.thedaymarket.domain.TransactionType;
import com.thedaymarket.domain.User;
import com.thedaymarket.domain.UserPoints;
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

  @Override
  public Transaction buyPoints(BuyPointsRequest buyPoints, Long userId) {
    var creditCardDetails = buyPoints.creditCardDetails();
    var buyer = userService.getUser(userId);
    var transactionResponse =
        paymentSimulator.createCreditCardTransaction(creditCardDetails, buyPoints.amount());
    if (transactionResponse.success()) {
      var transaction =
          createTransaction(
              buyPoints.amount(), buyer, userService.getSystemUser(), transactionResponse);

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
              sellPoints.points(), userService.getSystemUser(), seller, transactionResponse);

      updateUserPoints(sellPoints.points(), seller, false);

      return transaction;
    } else {
      throw new WebServerException("Transaction failed for user: " + userId, new Throwable());
    }
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

  private Transaction createTransaction(
      BigDecimal buyPoints, User buyer, User seller, TransactionResponse transactionResponse) {
    var transaction = new Transaction();
    transaction.setAmount(buyPoints);
    transaction.setType(TransactionType.POINTS);
    transaction.setBuyer(buyer);
    transaction.setSeller(seller);
    transaction.setAuction(null);
    transaction.setPaymentMethod(transactionResponse.paymentMethod());
    return transactionRepository.save(transaction);
  }
}

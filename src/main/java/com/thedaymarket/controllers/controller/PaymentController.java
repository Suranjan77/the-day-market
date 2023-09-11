package com.thedaymarket.controllers.controller;

import com.thedaymarket.controllers.request.BuyPointsRequest;
import com.thedaymarket.controllers.request.SellPointsRequest;
import com.thedaymarket.controllers.response.PointsTransactionResponse;
import com.thedaymarket.service.PaymentService;
import com.thedaymarket.utils.NameUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping("buy")
  public PointsTransactionResponse buyPoints(
      @RequestParam("userId") Long userId, @RequestBody @Valid BuyPointsRequest buyPointsReq) {

    var transaction = paymentService.buyPoints(buyPointsReq, userId);

    return new PointsTransactionResponse(
        transaction.getId(),
        NameUtils.getFullName(
            transaction.getBuyer().getFirstName(), transaction.getBuyer().getLastName()),
        "The Day Market",
        transaction.getAmount());
  }

  @PostMapping("sell")
  public PointsTransactionResponse sellPoints(
      @RequestParam("userId") Long userId, @RequestBody @Valid SellPointsRequest sellPointsRequest) {
    var transaction = paymentService.sellPoints(sellPointsRequest, userId);

    return new PointsTransactionResponse(
        transaction.getId(),
        "The Day Market",
        NameUtils.getFullName(
            transaction.getBuyer().getFirstName(), transaction.getBuyer().getLastName()),
        transaction.getAmount());
  }
}

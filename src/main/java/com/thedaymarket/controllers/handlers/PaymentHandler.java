package com.thedaymarket.controllers.handlers;

import com.thedaymarket.controllers.request.BuyPointsRequest;
import com.thedaymarket.controllers.request.SellPointsRequest;
import com.thedaymarket.controllers.response.PointsTransactionResponse;
import com.thedaymarket.service.PaymentService;
import com.thedaymarket.utils.NameUtils;
import com.thedaymarket.utils.RESTUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@AllArgsConstructor
@Component
public class PaymentHandler {

  private final PaymentService paymentService;
  private final Validator validator;

  public ServerResponse buyPoints(ServerRequest req) {
    var buyPointsReq = RESTUtils.parseRequestBody(req, BuyPointsRequest.class, validator);
    Long userId = Long.parseLong(RESTUtils.getRequiredParameter(req, "userId"));

    var transaction = paymentService.buyPoints(buyPointsReq, userId);

    return ServerResponse.ok()
        .body(
            new PointsTransactionResponse(
                transaction.getId(),
                NameUtils.getFullName(
                    transaction.getBuyer().getFirstName(), transaction.getBuyer().getLastName()),
                "The Day Market",
                transaction.getAmount()));
  }

  public ServerResponse sellPoints(ServerRequest req) {
    var sellPointsReq = RESTUtils.parseRequestBody(req, SellPointsRequest.class, validator);
    Long userId = Long.parseLong(RESTUtils.getRequiredParameter(req, "userId"));

    var transaction = paymentService.sellPoints(sellPointsReq, userId);

    return ServerResponse.ok()
        .body(
            new PointsTransactionResponse(
                transaction.getId(),
                "The Day Market",
                NameUtils.getFullName(
                    transaction.getBuyer().getFirstName(), transaction.getBuyer().getLastName()),
                transaction.getAmount()));
  }
}

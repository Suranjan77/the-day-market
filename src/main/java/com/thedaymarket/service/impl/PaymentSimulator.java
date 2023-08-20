package com.thedaymarket.service.impl;

import com.thedaymarket.controllers.request.BankAccountDetails;
import com.thedaymarket.controllers.request.CreditCardDetails;
import java.math.BigDecimal;
import java.util.UUID;

import com.thedaymarket.domain.PaymentMethod;
import org.springframework.stereotype.Service;

@Service
public class PaymentSimulator {
  public TransactionResponse createCreditCardTransaction(
      CreditCardDetails creditCardDetails, BigDecimal amount) {
    return new TransactionResponse(
        200, true, UUID.randomUUID().toString(), amount, PaymentMethod.CREDIT_CARD);
  }

  public TransactionResponse createBankAccountTransaction(
      BankAccountDetails bankAccountDetails, BigDecimal amount) {
    return new TransactionResponse(
        200, true, UUID.randomUUID().toString(), amount, PaymentMethod.BANK_TRANSFER);
  }
}

package com.thedaymarket.repository;

import com.thedaymarket.domain.Transaction;
import com.thedaymarket.domain.TransactionType;
import com.thedaymarket.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  Optional<Transaction> findByTypeAndSellerAndBuyerAndOrderId(
      TransactionType type, User seller, User buyer, Long orderId);
}

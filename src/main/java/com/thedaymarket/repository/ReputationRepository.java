package com.thedaymarket.repository;

import com.thedaymarket.domain.Reputation;
import com.thedaymarket.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReputationRepository extends JpaRepository<Reputation, Long> {
  Optional<Reputation> findByUser(User user);
}

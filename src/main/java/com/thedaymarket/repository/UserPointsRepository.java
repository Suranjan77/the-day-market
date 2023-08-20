package com.thedaymarket.repository;

import com.thedaymarket.domain.User;
import com.thedaymarket.domain.UserPoints;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPointsRepository extends JpaRepository<UserPoints, Long> {

  Optional<UserPoints> findByBelongsTo(User user);
}

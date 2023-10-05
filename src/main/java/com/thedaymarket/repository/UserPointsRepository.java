package com.thedaymarket.repository;

import com.thedaymarket.domain.User;
import com.thedaymarket.domain.UserPoints;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserPointsRepository extends JpaRepository<UserPoints, Long> {

  Optional<UserPoints> findByBelongsTo(User user);

  @Modifying
  @Query("UPDATE UserPoints u SET u.count= :points WHERE u.id = :id")
  void updateUserPointsAmount(@Param("points") BigDecimal points, @Param("id") Long id);
}

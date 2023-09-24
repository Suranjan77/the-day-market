package com.thedaymarket.controllers.response;

import com.thedaymarket.domain.User;
import com.thedaymarket.domain.UserAddress;
import com.thedaymarket.domain.UserRole;

import java.math.BigDecimal;

public record UserResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    UserAddress address,
    String profileImage,
    UserRole role,
    BigDecimal points,
    Double reputation) {
  public static UserResponse ofUser(User user) {
    return new UserResponse(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail(),
        user.getAddress(),
        user.getProfileImageName(),
        user.getRole(),
        user.getPoints(),
        user.getReputation() != null ? user.getReputation().getReputationPoints() : 0.0);
  }
}

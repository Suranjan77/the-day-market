package com.thedaymarket.domain;

import com.thedaymarket.utils.AuthConstants;
import jakarta.persistence.Embeddable;
import java.time.Instant;
import java.time.LocalDateTime;

import lombok.Data;

@Data
@Embeddable
public class UserAuth {
  private String token;
  private String password;
  private LocalDateTime lastLoggedInAt;

  public boolean isTokenValid() {
    return lastLoggedInAt
        .plusSeconds(AuthConstants.TOKEN_EXPIRY_SECONDS)
        .isAfter(LocalDateTime.now());
  }
}

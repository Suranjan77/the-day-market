package com.thedaymarket.domain;

import com.thedaymarket.utils.AuthConstants;
import jakarta.persistence.Embeddable;
import java.time.Instant;
import lombok.Data;

@Data
@Embeddable
public class UserAuth {
  private String token;
  private String password;
  private Instant lastLoggedInAt;

  public boolean isTokenValid() {
    return lastLoggedInAt
        .plusMillis(AuthConstants.TOKEN_EXPIRY_MILLISECONDS)
        .isAfter(Instant.now());
  }
}

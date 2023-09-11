package com.thedaymarket.utils;

import java.time.Duration;

public final class AuthConstants {
  private AuthConstants() {}

  public static final Long TOKEN_EXPIRY_SECONDS = Duration.ofHours(1L).toSeconds();
  public static final String JWT_TOKEN_SECRET = "SWFtYUp3dE9mTWFya2V0";
  public static final String AUTH_ERROR_MESSAGE = "Invalid credentials";
}

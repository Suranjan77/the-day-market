package com.thedaymarket.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.thedaymarket.service.TokenService;
import com.thedaymarket.utils.AuthConstants;
import com.thedaymarket.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class JwtTokenService implements TokenService {

  private final Algorithm hmac512;
  private final JWTVerifier verifier;

  public JwtTokenService() {
    this.hmac512 = Algorithm.HMAC512(AuthConstants.JWT_TOKEN_SECRET);
    this.verifier = JWT.require(this.hmac512).build();
  }

  @Override
  public String generateToken(UserDetails userDetails) {
    final Instant now = Instant.now();
    return JWT.create()
        .withSubject(userDetails.getUsername())
        .withIssuer("thedaymarket")
        .withIssuedAt(now)
        .withExpiresAt(now.plusMillis(AuthConstants.TOKEN_EXPIRY_MILLISECONDS))
        .sign(this.hmac512);
  }

  @Override
  public String validateTokenAndGetUserName(String token) {
    try {
      return verifier.verify(token).getSubject();
    } catch (final JWTVerificationException verificationEx) {
      log.warn("token invalid: {}", verificationEx.getMessage());
      throw ExceptionUtils.getAuthenticationException(AuthConstants.AUTH_ERROR_MESSAGE);
    }
  }
}

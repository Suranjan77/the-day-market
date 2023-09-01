package com.thedaymarket.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
  String generateToken(UserDetails userDetails);

  String validateTokenAndGetUserName(String token);
}

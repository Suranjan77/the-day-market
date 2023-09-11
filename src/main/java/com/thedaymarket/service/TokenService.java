package com.thedaymarket.service;


public interface TokenService {
  String generateToken(String userName);

  String validateTokenAndGetUserName(String token);
}

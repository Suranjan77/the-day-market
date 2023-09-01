package com.thedaymarket.service;

import com.thedaymarket.controllers.request.AuthRequest;
import com.thedaymarket.controllers.response.AuthResponse;

public interface AuthService {
  AuthResponse authenticate(AuthRequest authRequest);
}

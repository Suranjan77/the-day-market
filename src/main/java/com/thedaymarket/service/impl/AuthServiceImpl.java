package com.thedaymarket.service.impl;

import com.thedaymarket.controllers.request.AuthRequest;
import com.thedaymarket.controllers.response.AuthResponse;
import com.thedaymarket.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        return null;
    }
}

package com.thedaymarket.controllers.controller;

import com.thedaymarket.controllers.request.AuthRequest;
import com.thedaymarket.controllers.request.UserRegisterRequest;
import com.thedaymarket.controllers.response.AuthResponse;
import com.thedaymarket.controllers.response.UserCreatedResponse;
import com.thedaymarket.service.TokenService;
import com.thedaymarket.service.UserService;
import com.thedaymarket.service.impl.JwtUserDetails;
import com.thedaymarket.utils.AuthConstants;
import com.thedaymarket.utils.ExceptionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
  private final UserDetailsService userDetailsService;
  private final TokenService tokenService;
  private final AuthenticationManager authManager;
  private final UserService userService;

  @PostMapping("login")
  public AuthResponse login(@RequestBody @Valid AuthRequest authRequest) {
    try {
      authManager.authenticate(
          new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password()));
    } catch (BadCredentialsException ex) {
      throw ExceptionUtils.getAuthenticationException(AuthConstants.AUTH_ERROR_MESSAGE);
    }

    var userDetails = (JwtUserDetails) userDetailsService.loadUserByUsername(authRequest.email());
    var token = tokenService.generateToken(userDetails.getUsername());
    var role =
        userDetails.getAuthorities().isEmpty()
            ? ""
            : userDetails.getAuthorities().stream().findFirst().get().toString();

    userService.updateLoggedInTimeAndToken(userDetails.getId(), token, LocalDateTime.now());

    return new AuthResponse(
        userDetails.getId(),
        token,
        role,
        AuthConstants.TOKEN_EXPIRY_SECONDS,
        userDetails.getFirstName(),
        userDetails.getLastName(),
        userDetails.getUsername(),
        userDetails.getProfileImage(),
        userDetails.getPoints(),
        userDetails.getAddress(),
        userDetails.isFirstLogin());
  }

  @PostMapping("refresh-token")
  public AuthResponse refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
    final String token = authHeader.substring(7);
    var user = userService.getByToken(token);
    var newToken = tokenService.generateToken(user.getEmail());
    userService.updateLoggedInTimeAndToken(user.getId(), newToken, LocalDateTime.now());
    return new AuthResponse(
        user.getId(),
        newToken,
        user.getRole().name(),
        AuthConstants.TOKEN_EXPIRY_SECONDS,
        user.getFirstName(),
        user.getLastName(),
        user.getEmail(),
        user.getProfileImageName(),
        user.getPoints(),
        user.getAddress(),
        user.isFirstLogin());
  }

  @PostMapping("register")
  public UserCreatedResponse register(@RequestBody @Valid UserRegisterRequest registerRequest) {
    var user = userService.createUser(registerRequest);
    return new UserCreatedResponse(
        user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
  }
}

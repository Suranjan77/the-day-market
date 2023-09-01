package com.thedaymarket.controllers.handlers;

import com.thedaymarket.controllers.request.AuthRequest;
import com.thedaymarket.controllers.request.UserRegisterRequest;
import com.thedaymarket.controllers.response.AuthResponse;
import com.thedaymarket.controllers.response.UserCreatedResponse;
import com.thedaymarket.service.TokenService;
import com.thedaymarket.service.UserService;
import com.thedaymarket.service.impl.JwtUserDetails;
import com.thedaymarket.utils.AuthConstants;
import com.thedaymarket.utils.ExceptionUtils;
import com.thedaymarket.utils.RESTUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@AllArgsConstructor
@Component
public class AuthHandler {
  private final UserDetailsService userDetailsService;
  private final TokenService tokenService;
  private final Validator validator;
  private final AuthenticationManager authManager;
  private final UserService userService;

  public ServerResponse login(ServerRequest req) {
    var authRequest = RESTUtils.parseRequestBody(req, AuthRequest.class, validator);

    try {
      authManager.authenticate(
          new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password()));
    } catch (BadCredentialsException ex) {
      throw ExceptionUtils.getAuthenticationException(AuthConstants.AUTH_ERROR_MESSAGE);
    }

    var userDetails = userDetailsService.loadUserByUsername(authRequest.email());
    var token = tokenService.generateToken(userDetails);
    var role =
        userDetails.getAuthorities().isEmpty()
            ? ""
            : userDetails.getAuthorities().stream().findFirst().toString();

    return ServerResponse.ok()
        .body(new AuthResponse(token, role, AuthConstants.TOKEN_EXPIRY_MILLISECONDS));
  }

  public ServerResponse register(ServerRequest req) {
    var registerRequest = RESTUtils.parseRequestBody(req, UserRegisterRequest.class, validator);
    var user = userService.createUser(registerRequest);
    return ServerResponse.ok()
        .body(
            new UserCreatedResponse(
                user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()));
  }
}

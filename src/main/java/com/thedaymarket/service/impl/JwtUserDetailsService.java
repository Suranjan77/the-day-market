package com.thedaymarket.service.impl;

import com.thedaymarket.repository.UserRepository;
import com.thedaymarket.utils.AuthConstants;
import com.thedaymarket.utils.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user =
        userRepository
            .findByEmail(username)
            .orElseThrow(
                () -> ExceptionUtils.getAuthenticationException(AuthConstants.AUTH_ERROR_MESSAGE));
    List<SimpleGrantedAuthority> roles = new ArrayList<>();
    if (user.getRole() != null) {
      roles.add(new SimpleGrantedAuthority(user.getRole().name()));
    }

    return new JwtUserDetails(
        user.getId(),
        user.isFirstLogin(),
        username,
        user.getAuth().getPassword(),
        roles,
        user.getFirstName(),
        user.getLastName(),
        user.getProfileImageName(),
        user.getPoints(),
        user.getAddress());
  }

  public boolean isTokenValid(String token) {
    var user =
        userRepository
            .findByAuthToken(token)
            .orElseThrow(
                () -> ExceptionUtils.getAuthenticationException(AuthConstants.AUTH_ERROR_MESSAGE));

    return user.getAuth() != null && user.getAuth().isTokenValid();
  }
}

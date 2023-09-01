package com.thedaymarket.service.impl;

import com.thedaymarket.repository.UserRepository;
import com.thedaymarket.utils.AuthConstants;
import com.thedaymarket.utils.ExceptionUtils;
import java.util.Collections;
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

    List<SimpleGrantedAuthority> roles =
        Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));

    return new JwtUserDetails(user.getId(), username, user.getAuth().getPassword(), roles);
  }
}

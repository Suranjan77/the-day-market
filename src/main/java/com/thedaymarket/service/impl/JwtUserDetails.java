package com.thedaymarket.service.impl;

import java.math.BigDecimal;
import java.util.Collection;

import com.thedaymarket.domain.UserAddress;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class JwtUserDetails extends User {
  private final Long id;
  private final boolean isFirstLogin;
  private final String firstName;
  private final String lastName;
  private final String profileImage;
  private final BigDecimal points;
  private final UserAddress address;

  public JwtUserDetails(
      Long id,
      boolean isFirstLogin,
      String username,
      String password,
      Collection<? extends GrantedAuthority> authorities,
      String firstName,
      String lastName,
      String profileImage,
      BigDecimal points,
      UserAddress address) {
    super(username, password, authorities);
    this.id = id;
    this.isFirstLogin = isFirstLogin;
    this.firstName = firstName;
    this.lastName = lastName;
    this.profileImage = profileImage;
    this.points = points;
    this.address = address;
  }
}

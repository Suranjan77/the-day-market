package com.thedaymarket.service.impl;

import com.thedaymarket.controllers.request.UserUpdateRequest;
import com.thedaymarket.domain.User;
import com.thedaymarket.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  @Override
  public User getUser(Long userId) {
    return null;
  }

  @Override
  public User getSystemUser() {
    return null;
  }

  @Override
  public User updateUser(Long userId, UserUpdateRequest updateRequest) {
    return null;
  }

  @Override
  public void deleteUser(Long userId) {}
}

package com.thedaymarket.service;

import com.thedaymarket.controllers.request.UserUpdateRequest;
import com.thedaymarket.domain.User;

public interface UserService {
  User getUser(Long userId);

  User getSystemUser();

  User updateUser(Long userId, UserUpdateRequest updateRequest);

  void deleteUser(Long userId);
}

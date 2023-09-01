package com.thedaymarket.service;

import com.thedaymarket.controllers.request.UserRegisterRequest;
import com.thedaymarket.controllers.request.UserUpdateRequest;
import com.thedaymarket.domain.User;

public interface UserService {

  User createUser(UserRegisterRequest userRequest);

  User getUser(Long userId);

  User getUserByEmail(String email);

  User getSystemUser();

  User updateUser(Long userId, UserUpdateRequest updateRequest);

  void deleteUser(Long userId);
}

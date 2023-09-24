package com.thedaymarket.service;

import com.thedaymarket.controllers.request.UserRegisterRequest;
import com.thedaymarket.controllers.request.UserUpdateRequest;
import com.thedaymarket.domain.User;
import java.time.LocalDateTime;

import com.thedaymarket.domain.UserPoints;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  User createUser(UserRegisterRequest userRequest);

  User getUser(Long userId);

  User uploadProfileImage(Long userId, MultipartFile image);

  User updateLoggedInTimeAndToken(Long userId, String token, LocalDateTime instant);

  User getByToken(String token);

  User getUserByEmail(String email);

  User getSystemUser();

  User updateUser(Long userId, UserUpdateRequest updateRequest);

  void deleteUser(Long userId);
}

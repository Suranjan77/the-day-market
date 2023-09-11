package com.thedaymarket.service.impl;

import com.thedaymarket.controllers.request.UserRegisterRequest;
import com.thedaymarket.controllers.request.UserUpdateRequest;
import com.thedaymarket.domain.User;
import com.thedaymarket.domain.UserAuth;
import com.thedaymarket.repository.UserRepository;
import com.thedaymarket.service.StorageService;
import com.thedaymarket.service.UserService;
import com.thedaymarket.utils.ExceptionUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final StorageService storageService;

  @Override
  public User createUser(UserRegisterRequest userRequest) {
    if (findUserByEmail(userRequest.email()).isPresent()) {
      throw ExceptionUtils.getBadRequestExceptionResponse(
          "User already exists with email: " + userRequest.email());
    }
    var user = new User();
    user.setEmail(userRequest.email());
    user.setFirstName(userRequest.firstName());
    user.setLastName(userRequest.lastName());

    var auth = new UserAuth();
    auth.setPassword(passwordEncoder.encode(userRequest.password()));
    user.setAuth(auth);
    return userRepository.save(user);
  }

  @Override
  public User getUser(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(
            () -> ExceptionUtils.getNotFoundExceptionResponse("User not found with id: " + userId));
  }

  @Override
  public User uploadProfileImage(Long userId, MultipartFile image) {
    var user = getUser(userId);
    try {
      var uploadResponse = storageService.upload(image);
      user.setProfileImageName(uploadResponse.fileName());
      userRepository.save(user);
    } catch (Exception e) {
      throw ExceptionUtils.getServerException(e.getMessage());
    }

    return user;
  }

  @Override
  public User updateLoggedInTimeAndToken(Long userId, String token, LocalDateTime instant) {
    var user = getUser(userId);

    if (user.getAuth() == null) {
      log.warn("User without auth should not be able to login.");
    }

    var userAuth = user.getAuth();
    userAuth.setLastLoggedInAt(instant);
    userAuth.setToken(token);
    user.setAuth(userAuth);
    return userRepository.save(user);
  }

  @Override
  public User getByToken(String token) {
    return userRepository
        .findByAuthToken(token)
        .orElseThrow(() -> ExceptionUtils.getAuthenticationException("Token invalid"));
  }

  @Override
  public User getUserByEmail(String email) {
    return findUserByEmail(email)
        .orElseThrow(
            () ->
                ExceptionUtils.getNotFoundExceptionResponse("User not found with email: " + email));
  }

  public Optional<User> findUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public User getSystemUser() {
    return null;
  }

  @Override
  public User updateUser(Long userId, UserUpdateRequest updateRequest) {
    var user = getUser(userId);
    user.setRole(updateRequest.role());
    user.setAddress(updateRequest.address());
    return userRepository.save(user);
  }

  @Override
  public void deleteUser(Long userId) {
    userRepository.deleteById(userId);
  }
}

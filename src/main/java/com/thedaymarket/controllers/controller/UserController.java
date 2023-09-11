package com.thedaymarket.controllers.controller;

import com.thedaymarket.controllers.request.UserUpdateRequest;
import com.thedaymarket.controllers.response.UserResponse;
import com.thedaymarket.service.UserService;
import com.thedaymarket.utils.ExceptionUtils;
import com.thedaymarket.utils.JsonUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

  private final UserService userService;

  @PostMapping("{id}/details")
  public UserResponse updateUserDetails(
      @PathVariable("id") Long userId, @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
    var user = userService.updateUser(userId, userUpdateRequest);
    var response = UserResponse.ofUser(user);
    log.debug("Updated user {}", JsonUtils.toJson(response));
    return response;
  }

  @PostMapping("{id}/image")
  public UserResponse uploadProfileImage(
      @PathVariable("id") Long userId, @RequestParam("file") MultipartFile image) {
    if (image != null) {
      var user = userService.uploadProfileImage(userId, image);
      return UserResponse.ofUser(user);
    }

    throw ExceptionUtils.getBadRequestExceptionResponse("File not uploaded.");
  }
}

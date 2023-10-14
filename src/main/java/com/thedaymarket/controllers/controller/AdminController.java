package com.thedaymarket.controllers.controller;

import com.thedaymarket.repository.AuctionRepository;
import com.thedaymarket.utils.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/admin")
public class AdminController {

  private final AuctionRepository auctionRepository;

  @Value("${admin.api-key}")
  private String adminApiKey;

  @GetMapping("set-auctions-to-today")
  public void setAuctionToday(@RequestHeader("api-key") String apiKey) {
    verifyAdminApiKey(apiKey);
    auctionRepository.updateAllAuctionsDate();
  }

  private void verifyAdminApiKey(String apiKey) {
    if (!apiKey.equals(adminApiKey)) {
      throw ExceptionUtils.getAuthenticationException(
          "API key invalid or not provided as api-key header");
    }
  }
}
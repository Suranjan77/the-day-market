package com.thedaymarket.controllers;

import java.time.Instant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
  @GetMapping("/health-check")
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("{\"status\": \"OK\", \"timeStamp\": " + Instant.now() + " }");
  }
}

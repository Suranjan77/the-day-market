package com.thedaymarket.controllers.controller;

import com.google.common.net.HttpHeaders;
import com.thedaymarket.service.StorageService;
import com.thedaymarket.utils.ExceptionUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/image")
public class ImagesController {

  private final StorageService storageService;

  @GetMapping
  public ResponseEntity<byte[]> download(@RequestParam("fileName") String fileName) {
    try {
      var downloadedObject = storageService.download(fileName);
      byte[] image;
      try (var imageStream = downloadedObject.stream()) {
        image = imageStream.readAllBytes();
      }
      return ResponseEntity.ok()
          .headers(h -> h.set(HttpHeaders.CONTENT_TYPE, downloadedObject.contentType()))
          .body(image);
    } catch (Exception e) {
      throw ExceptionUtils.getServerException("Couldn't download image with name: " + fileName);
    }
  }
}

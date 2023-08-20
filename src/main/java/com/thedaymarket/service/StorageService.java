package com.thedaymarket.service;

import com.thedaymarket.service.impl.DownloadedObject;
import com.thedaymarket.service.impl.UploadedObject;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
  UploadedObject upload(MultipartFile filePart) throws Exception;

  DownloadedObject download(String fileName) throws Exception;

  void delete(String fileName) throws Exception;
}

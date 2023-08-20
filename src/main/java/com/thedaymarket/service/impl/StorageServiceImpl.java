package com.thedaymarket.service.impl;

import com.thedaymarket.service.StorageService;
import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

  private final MinioClient storageClient;
  private final String BUCKET = "thedaymarket";
  private final String storageUrl;

  public StorageServiceImpl(
      @Value("${minio.storage.url}") String storageUrl,
      @Value("${minio.storage.username}") String username,
      @Value("${minio.storage.password}") String password) {
    this.storageUrl = storageUrl;
    this.storageClient =
        MinioClient.builder().endpoint(storageUrl).credentials(username, password).build();
    initBucket();
  }

  public UploadedObject upload(MultipartFile filePart) throws Exception {

    try (InputStream inputStream = filePart.getInputStream()) {
      String fileName =
          UUID.randomUUID().toString().replaceAll("-", "_")
              + "_"
              + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"))
              + "_"
              + filePart
                  .getOriginalFilename()
                  .substring(filePart.getOriginalFilename().lastIndexOf("."));

      PutObjectArgs putObjectOptions =
          PutObjectArgs.builder()
              .bucket(BUCKET)
              .object(fileName)
              .contentType(filePart.getContentType())
              .stream(inputStream, filePart.getSize(), -1)
              .build();

      storageClient.putObject(putObjectOptions);

      return new UploadedObject(
          fileName,
          storageUrl + "/" + BUCKET + "/" + UriUtils.encode(fileName, StandardCharsets.UTF_8));
    }
  }

  public DownloadedObject download(String fileName) throws Exception {
    StatObjectResponse stat =
        storageClient.statObject(StatObjectArgs.builder().bucket(BUCKET).object(fileName).build());
    InputStream in =
        storageClient.getObject(GetObjectArgs.builder().bucket(BUCKET).object(fileName).build());
    return new DownloadedObject(stat.contentType(), in);
  }

  public void delete(String fileName) throws Exception {
    storageClient.removeObject(RemoveObjectArgs.builder().bucket(BUCKET).object(fileName).build());
  }

  private void initBucket() {
    try {
      boolean found = storageClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET).build());
      if (!found) {
        log.info("Creating minio bucket {}", BUCKET);
        storageClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET).build());
      } else {
        log.info("Minio bucket {} exists", BUCKET);
      }
    } catch (ErrorResponseException
        | InsufficientDataException
        | InternalException
        | InvalidKeyException
        | InvalidResponseException
        | IOException
        | NoSuchAlgorithmException
        | ServerException
        | XmlParserException e) {
      throw new RuntimeException(e);
    }
  }
}

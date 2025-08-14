package com.mobigen.framework.utility;

import io.minio.MinioClient;

public class MinioFactory {
  public static MinioClient create(String endpoint, String accessKey, String secretKey) {
    return MinioClient.builder()
        .endpoint(endpoint)                 // http://minio:9000
        .credentials(accessKey, secretKey)  // minioAccessKey / minioSecretKey
        .build();
  }
}
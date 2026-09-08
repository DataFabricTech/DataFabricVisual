package com.mobigen.datalake.metaData.manage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mobigen.framework.utility.MinioFactory;

import io.minio.BucketExistsArgs;
import io.minio.GetBucketLifecycleArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketLifecycleArgs;
import io.minio.messages.Expiration;
import io.minio.messages.LifecycleConfiguration;
import io.minio.messages.LifecycleRule;
import io.minio.messages.ResponseDate;
import io.minio.messages.RuleFilter;
import io.minio.messages.Status;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MinioPendingUploadLifecycleInitializer {

    private static final String RULE_ID = "expire-pending-uploads";

    private static final int EXPIRE_DAYS = 1;

    @Value("${properties.minio.endpoint}")
    private String endpoint;

    @Value("${properties.minio.accessKey}")
    private String accessKey;

    @Value("${properties.minio.secretKey}")
    private String secretKey;

    @Value("${properties.minio.unstructured_bucket}")
    private String unstructuredBucket;

    @PostConstruct
    public void registerPendingUploadExpiryRule() {
        try {
            MinioClient minioClient = MinioFactory.create(endpoint, accessKey, secretKey);

            boolean isExist = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(unstructuredBucket).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(unstructuredBucket).build());
                log.info("비정형 버킷 생성: {}", unstructuredBucket);
            }

            List<LifecycleRule> rules = new ArrayList<>();
            LifecycleConfiguration current = minioClient.getBucketLifecycle(
                    GetBucketLifecycleArgs.builder().bucket(unstructuredBucket).build());
            if (current != null && current.rules() != null) {
                for (LifecycleRule rule : current.rules()) {
                    if (!RULE_ID.equals(rule.id())) {
                        rules.add(rule);
                    }
                }
            }

            rules.add(new LifecycleRule(
                    Status.ENABLED,
                    null,
                    new Expiration((ResponseDate) null, EXPIRE_DAYS, null),
                    new RuleFilter(MetaDataManageService.PENDING_OBJECT_PREFIX),
                    RULE_ID,
                    null,
                    null,
                    null));

            minioClient.setBucketLifecycle(SetBucketLifecycleArgs.builder()
                    .bucket(unstructuredBucket)
                    .config(new LifecycleConfiguration(rules))
                    .build());

            log.info("MinIO lifecycle 규칙 등록 완료: bucket={}, prefix={}, expireDays={}",
                    unstructuredBucket, MetaDataManageService.PENDING_OBJECT_PREFIX, EXPIRE_DAYS);

        } catch (Exception e) {
            log.error("MinIO lifecycle 규칙 등록 실패. pending/ 임시 객체가 자동 정리되지 않는다. "
                    + "bucket={}", unstructuredBucket, e);
        }
    }
}

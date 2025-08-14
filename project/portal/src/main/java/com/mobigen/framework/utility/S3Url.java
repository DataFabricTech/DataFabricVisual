package com.mobigen.framework.utility;

public class S3Url {
    private final String bucket;
    private final String object;

    public S3Url(String bucket, String object) {
        this.bucket = bucket;
        this.object = object;
    }

    public String getBucket() { return bucket; }
    public String getObject() { return object; }

    public static S3Url parse(String s3url) {
        if (s3url == null) {
            throw new IllegalArgumentException("invalid s3 url");
        } else if (!s3url.startsWith("s3://")) {
            throw new IllegalArgumentException("invalid s3 url");
        } else {
            String noScheme = s3url.substring(5);
            int i = noScheme.indexOf('/');
            if (i < 0) return new S3Url(noScheme, "");
            return new S3Url(noScheme.substring(0, i), noScheme.substring(i + 1));
        }
    }
}
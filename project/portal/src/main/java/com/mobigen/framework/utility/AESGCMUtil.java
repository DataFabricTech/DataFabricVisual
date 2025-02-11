package com.mobigen.framework.utility;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class AESGCMUtil {
    private static final int GCM_IV_LENGTH = 12;  // IV (Nonce) 길이
    private static final int GCM_TAG_LENGTH = 16; // GCM 태그 길이
    private final SecretKey secretKey;

    // 생성자에서 키를 설정
    public AESGCMUtil(String cryptKey) throws Exception {
        this.secretKey = generateSecretKey(cryptKey);
    }

    // SHA-256을 이용하여 32바이트 AES 키 생성
    private SecretKey generateSecretKey(String cryptKey) throws Exception {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha256.digest(cryptKey.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, "AES");
    }

    // AES-GCM 암호화 (IV를 자동 생성)
    public String encrypt(String data) throws Exception {
        // 랜덤 IV 생성
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        // AES-GCM 암호화 수행
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // IV + 암호문 조합하여 Base64 인코딩
        byte[] result = new byte[GCM_IV_LENGTH + encryptedData.length];
        System.arraycopy(iv, 0, result, 0, GCM_IV_LENGTH);
        System.arraycopy(encryptedData, 0, result, GCM_IV_LENGTH, encryptedData.length);

        return Base64.getEncoder().encodeToString(result);
    }

    // AES-GCM 복호화 (암호문에서 IV 추출)
    public String decrypt(String encryptedBase64) throws Exception {
        byte[] encryptedDataWithIv = Base64.getDecoder().decode(encryptedBase64);

        // IV와 암호문 분리
        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] encryptedData = new byte[encryptedDataWithIv.length - GCM_IV_LENGTH];

        System.arraycopy(encryptedDataWithIv, 0, iv, 0, GCM_IV_LENGTH);
        System.arraycopy(encryptedDataWithIv, GCM_IV_LENGTH, encryptedData, 0, encryptedData.length);

        // AES-GCM 복호화 수행
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

        return new String(cipher.doFinal(encryptedData), StandardCharsets.UTF_8);
    }
}

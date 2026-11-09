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
        new SecureRandom().nextBytes(iv);

        // AES-GCM 암호화 수행
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // 암호문에서 태그, 암호문 추출
        byte[] authTag = new byte[GCM_TAG_LENGTH];
        byte[] cipherTextWithoutTag = new byte[encryptedData.length - GCM_TAG_LENGTH];
        System.arraycopy(encryptedData, encryptedData.length - GCM_TAG_LENGTH, authTag, 0, GCM_TAG_LENGTH);
        System.arraycopy(encryptedData, 0, cipherTextWithoutTag, 0, cipherTextWithoutTag.length);

        // **Python과 연동을 위해 IV + 태그 + 암호문 순서로 재조합
        byte[] result = new byte[GCM_IV_LENGTH + GCM_TAG_LENGTH + cipherTextWithoutTag.length];
        System.arraycopy(iv, 0, result, 0, GCM_IV_LENGTH); // IV 복사
        System.arraycopy(authTag, 0, result, GCM_IV_LENGTH, GCM_TAG_LENGTH); // 태그 복사
        System.arraycopy(cipherTextWithoutTag, 0, result, GCM_IV_LENGTH + GCM_TAG_LENGTH, cipherTextWithoutTag.length); // 암호문 복사

        return Base64.getEncoder().encodeToString(result);
    }

    // AES-GCM 복호화
    public String decrypt(String encryptedBase64) throws Exception {
        // Base64 디코딩을 통해 암호화된 데이터를 바이트 배열로 변환
        byte[] encryptedDataWithIv = Base64.getDecoder().decode(encryptedBase64);
    
        // IV와 암호문 분리 (암호문 앞부분이 IV, 뒤에는 태그 + 암호문)
        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] encryptedData = new byte[encryptedDataWithIv.length - GCM_IV_LENGTH];
        System.arraycopy(encryptedDataWithIv, 0, iv, 0, GCM_IV_LENGTH); // IV 추출
        System.arraycopy(encryptedDataWithIv, GCM_IV_LENGTH, encryptedData, 0, encryptedData.length); // 암호문 + 인증 태그 추출
    
        // 태그, 암호문 분리
        byte[] authTag = new byte[GCM_TAG_LENGTH];
        byte[] cipherTextWithoutTag = new byte[encryptedData.length - GCM_TAG_LENGTH];
        System.arraycopy(encryptedData, 0, authTag, 0, GCM_TAG_LENGTH); // 인증 태그
        System.arraycopy(encryptedData, GCM_TAG_LENGTH, cipherTextWithoutTag, 0, encryptedData.length - GCM_TAG_LENGTH); // 암호문

        // **Java에서는 복호화 시 암호문+태그 순서로 조합한 데이터를 복호화 요청해야함
        byte[] newEncryptedData = new byte [GCM_TAG_LENGTH + cipherTextWithoutTag.length];
        System.arraycopy(cipherTextWithoutTag, 0, newEncryptedData, 0, cipherTextWithoutTag.length);
        System.arraycopy(authTag, 0, newEncryptedData, cipherTextWithoutTag.length, GCM_TAG_LENGTH);
    
        // AES-GCM 복호화 수행
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv); // GCMTagLength는 비트 단위
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
    
        byte[] decryptedData = cipher.doFinal(newEncryptedData); // 복호화
    
        return new String(decryptedData, StandardCharsets.UTF_8); // 결과를 문자열로 변환
    }
}

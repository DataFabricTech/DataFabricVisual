package com.mobigen.framework.utility;

import org.jasypt.encryption.StringEncryptor;

public class AESGCMEncryptor implements StringEncryptor {
    
    private final AESGCMUtil aesGcmUtil;

    public AESGCMEncryptor(String secretKey) throws Exception {
        this.aesGcmUtil = new AESGCMUtil(secretKey);
    }

    @Override
    public String encrypt(String message) {
        try {
            return aesGcmUtil.encrypt(message);
        } catch (Exception e) {
            throw new RuntimeException("암호화에 실패했습니다.", e);
        }
    }

    @Override
    public String decrypt(String encryptedMessage) {
        try {
            return aesGcmUtil.decrypt(encryptedMessage);
        } catch (Exception e) {
            throw new RuntimeException("복호화에 실패했습니다.", e);
        }
    }

}
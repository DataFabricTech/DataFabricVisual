package com.mobigen.framework.utility;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public final class RSA {
    @Getter
    private KeyPair keyPair;

    public RSA() {
        this.keyPair = this.generateKeyPair();
    }

    public KeyPair generateKeyPair() {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator generator = null;
            
            generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048, new SecureRandom());
            keyPair = generator.genKeyPair();
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        }
        return keyPair;
    }

//    public KeyPair generateKeyPair(String base64PublicKey, String base64PrivateKey) {
//        PublicKey publicKey = this.getPublicKeyFromBase64Encrypted(base64PublicKey);
//        PrivateKey privateKey = this.getPrivateKeyFromBase64Encrypted(base64PrivateKey);
//
//        KeyPair keyPair = new KeyPair(publicKey, privateKey);
//        return keyPair;
//    }

    public String encryptRSA(String plainText, PublicKey publicKey) {
        Cipher cipher = null;
        String result = null;
        try {
//            cipher = Cipher.getInstance("RSA");
            cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");    // 2024-10-10 취약점 진단 조치
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] bytePlain = cipher.doFinal(plainText.getBytes());

            result = Base64.getEncoder().encodeToString(bytePlain);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        }
        return result;
    }

    public String decryptRSA(String encrypted, PrivateKey privateKey) {
        Cipher cipher = null;
        String result = null;
        try {
//            cipher = Cipher.getInstance("RSA");
            cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");    // 2024-10-10 취약점 진단 조치
            byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());


            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bytePlain = cipher.doFinal(byteEncrypted);

            result = new String(bytePlain, "utf-8");
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        }
        return result;
    }

    public String getBase64PublicKeyFromKeyPair(KeyPair keyPair) {
        PublicKey key = keyPair.getPublic();
        String base64 = Base64.getEncoder().encodeToString(key.getEncoded());
        return base64;
    }

//    public PublicKey getPublicKeyFromBase64Encrypted(String base64PublicKey) {
//        byte[] decodedBase64PubKey = Base64.getDecoder().decode(base64PublicKey);
//        PublicKey key = null;
//        try {
//            key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decodedBase64PubKey));
//        } catch (Exception e) {
//            log.error(e.getMessage(), e.getCause());
//        }
//        return key;
//    }

//    public String getBase64PrivateKeyFromKeyPair(KeyPair keyPair) {
//        PrivateKey key = keyPair.getPrivate();
//        String base64 = Base64.getEncoder().encodeToString(key.getEncoded());
//        return base64;
//    }
//
//    public PrivateKey getPrivateKeyFromBase64Encrypted(String base64PrivateKey) {
//        byte[] decodedBase64PrivateKey = Base64.getDecoder().decode(base64PrivateKey);
//        PrivateKey key = null;
//        try {
//            key = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decodedBase64PrivateKey));
//        } catch (Exception e) {
//            log.error(e.getMessage(), e.getCause());
//        }
//        return key;
//    }
}

package com.mobigen.framework.utility;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;


@Slf4j
public class Crypto {
    public static final String SHA = "SHA";
    public static final String SHA_224 = "SHA-224";
    public static final String SHA_256 = "SHA-256";
    public static final String SHA_384 = "SHA-384";
    public static final String SHA_512 = "SHA-512";

    private Crypto() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 해쉬 방식에서 사용하는 salt 생성
     *
     * @param length
     * @return
     * @throws Exception
     */
    public static String generateBase64Salt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.encodeBase64String(bytes);
    }

    /**
     * SHA256 결과를 HEX 코드로 반환
     *
     * @param message
     * @return
     * @throws Exception
     */
    public static String encryptionWithSHA256ToHex(String message) throws Exception {
        return Hex.encodeHexString(encryptionWithSHA(message, SHA_256, ""));
    }

    /**
     * SHA256 결과를 Base64로 반환
     *
     * @param message
     * @return
     * @throws Exception
     */
    public static String encryptionWithSHA256ToBase64(String message) throws Exception {
        return Base64.encodeBase64String(encryptionWithSHA(message, SHA_256, ""));
    }

    /**
     * SHA 암호화를 주어진 길이와 Salt 값으로 byte로 반환
     *
     * @param message
     * @param algorithm
     * @param salt
     * @return
     * @throws Exception
     */
    public static byte[] encryptionWithSHA(String message, String algorithm, String salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        String messageWithSalt = message + salt;
        md.update(messageWithSalt.getBytes());
        return md.digest();
    }

    /**
     * AES 암호화를 Hex로 반환
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptionWithAESToHex(String message, String key) throws Exception {
        return Hex.encodeHexString(encryptionWithAES(message, key.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * AES 암호화를 Base64로 반환
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptionWithAESToBase64(String message, String key) throws Exception {
        return Base64.encodeBase64String(encryptionWithAES(message, key.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * AES 암호화를 byte 로 반환
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptionWithAES(String message, byte[] key) throws Exception {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        Key k = new SecretKeySpec(key, "AES");
        IvParameterSpec iv = new IvParameterSpec(key);
        c.init(Cipher.ENCRYPT_MODE, k, iv);

        return c.doFinal(message.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * AES 암호화된 Hex 값을 복호화
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptionWithHexAES(String message, String key) throws Exception {
        return new String(decryptionWithAES(Hex.decodeHex(message), key.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * AES 암호화된 Base64 값을 복호화
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptionWithBase64AES(String message, String key) throws Exception {
        return new String(decryptionWithAES(Base64.decodeBase64(message), key.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * AES 암호화된 byte 값을 복호화
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptionWithAES(byte[] message, byte[] key) throws Exception {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        Key k = new SecretKeySpec(key, "AES");
        IvParameterSpec iv = new IvParameterSpec(key);
        c.init(Cipher.DECRYPT_MODE, k, iv);

        return c.doFinal(message);
    }
}

package com.mobigen.framework.utility;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import jakarta.xml.bind.DatatypeConverter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;


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
//    public static String generateBase64Salt(int length) {
//        SecureRandom random = new SecureRandom();
//        byte[] bytes = new byte[length];
//        random.nextBytes(bytes);
//        return DatatypeConverter.printBase64Binary(bytes);
//    }

    /**
     * SHA256 결과를 HEX 코드로 반환
     *
     * @param message
     * @return
     * @throws Exception
     */
//    public static String encryptionWithSHA256ToHex(String message) throws Exception {
//        return DatatypeConverter.printHexBinary(encryptionWithSHA(message, SHA_256, ""));
//    }

    /**
     * SHA256 결과를 Base64로 반환
     *
     * @param message
     * @return
     * @throws Exception
     */
//    public static String encryptionWithSHA256ToBase64(String message) throws Exception {
//        return DatatypeConverter.printBase64Binary(encryptionWithSHA(message, SHA_256, ""));
//    }

    /**
     * SHA 암호화를 주어진 길이와 Salt 값으로 byte로 반환
     *
     * @param message
     * @param algorithm
     * @param salt
     * @return
     * @throws Exception
     */
//    public static byte[] encryptionWithSHA(String message, String algorithm, String salt) throws Exception {
//        MessageDigest md = MessageDigest.getInstance(algorithm);
//        String messageWithSalt = message + salt;
//        md.update(messageWithSalt.getBytes());
//        return md.digest();
//    }

    /**
     * AES 암호화를 Hex로 반환
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
//    public static String encryptionWithAESToHex(String message, String key) throws Exception {
//        return DatatypeConverter.printHexBinary(encryptionWithAES(message, key.getBytes(StandardCharsets.UTF_8)));
//    }

    /**
     * AES 암호화를 Base64로 반환
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
//    public static String encryptionWithAESToBase64(String message, String key) throws Exception {
//        return DatatypeConverter.printBase64Binary(encryptionWithAES(message, key.getBytes(StandardCharsets.UTF_8)));
//    }

    /**
     * AES 암호화를 byte 로 반환
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
//    public static byte[] encryptionWithAES(String message, byte[] key) throws Exception {
//        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        Key k = new SecretKeySpec(key, "AES");
//        IvParameterSpec iv = new IvParameterSpec(key);
//        c.init(Cipher.ENCRYPT_MODE, k, iv);
//
//        return c.doFinal(message.getBytes(StandardCharsets.UTF_8));
//    }
    
    /**
     * AES 암호화된 Hex 값을 복호화
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
//    public static String decryptionWithHexAES(String message, String key) throws Exception {
//        return new String(decryptionWithAES(DatatypeConverter.parseHexBinary(message), key.getBytes(StandardCharsets.UTF_8)));
//    }

    /**
     * AES 암호화된 Base64 값을 복호화
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
//    public static String decryptionWithBase64AES(String message, String key) throws Exception {
//        return new String(decryptionWithAES(DatatypeConverter.parseBase64Binary(message), key.getBytes(StandardCharsets.UTF_8)));
//    }

    /**
     * AES 암호화된 byte 값을 복호화
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
//    public static byte[] decryptionWithAES(byte[] message, byte[] key) throws Exception {
//        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        Key k = new SecretKeySpec(key, "AES");
//        IvParameterSpec iv = new IvParameterSpec(key);
//        c.init(Cipher.DECRYPT_MODE, k, iv);
//
//        return c.doFinal(message);
//    }

//    /**
//     * ARIA 암호화를 Hex로 변환
//     *
//     * @param message
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public static String encryptionWithARIAToHex(String message, String key) throws Exception {
//        byte[] encrypted = encryptionWithARIA(message, key.getBytes(StandardCharsets.UTF_8));
//        return DatatypeConverter.printHexBinary(encrypted);
//    }
//
//    /**
//     * ARIA 암호화를 Base64로 변환
//     *
//     * @param message
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public static String encryptionWithARIAToBase64(String message, String key) throws Exception {
//        return DatatypeConverter.printBase64Binary(encryptionWithARIA(message, key.getBytes(StandardCharsets.UTF_8)));
//    }
//
//    /**
//     * ARIA 암호화를 byte로 변환
//     *
//     * @param message
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public static byte[] encryptionWithARIA(String message, byte[] key) throws Exception {
//        ARIAEngine aria = new ARIAEngine(key.length * 8);
//        aria.setKey(key);
//        aria.setupRoundKeys();
//
//        return aria.encrypt(message.getBytes(StandardCharsets.UTF_8), 0);
//    }
//
//
//    /**
//     * ARIA 암호화된 Hex 값을 복호화
//     *
//     * @param message
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public static String decryptionWithHexARIA(String message, String key) throws Exception {
//        return new String(decryptionWithARIA(DatatypeConverter.parseHexBinary(message), key.getBytes(StandardCharsets.UTF_8)));
//    }
//
//    /**
//     * ARIA 암호화된 Base64 값을 복호화
//     *
//     * @param message
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public static String decryptionWithBase64ARIA(String message, String key) throws Exception {
//        return new String(decryptionWithARIA(DatatypeConverter.parseBase64Binary(message), key.getBytes(StandardCharsets.UTF_8)));
//    }
//
//    /**
//     * ARIA 암호화된 텍스트 값을 복호화
//     *
//     * @param message
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public static byte[] decryptionWithARIA(byte[] message, byte[] key) throws Exception {
//        ARIAEngine aria = new ARIAEngine(key.length * 8);
//        aria.setKey(key);
//        aria.setupRoundKeys();
//
//        return aria.decrypt(message, 0);
//    }

//    public static String AES_Decode(String str)
//
//            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
//
//            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
//
//        String key = "M0bigenSecurity12#M0bigenSecurity12#";
//        byte[] ivBytes = new byte[16];
//        byte[] textBytes = Base64.decodeBase64(str);
//
//
//        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
//
//        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
//
//        Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding", "BC");
//
//        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
//
//        return new String(cipher.doFinal(textBytes), StandardCharsets.UTF_8);
//    }
}

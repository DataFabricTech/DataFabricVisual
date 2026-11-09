package com.mobigen.framework.utility;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.xml.bind.DatatypeConverter;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class CryptoTests {
    String plainText = "1234567890";
    String key = "1234567890123456";

//    @Test
//    @DisplayName("TEST CASE: SHA256/Base64")
//    public void SHA256Base64Test() {
//        try {
//            String encrypted = Crypto.encryptionWithSHA256ToBase64(plainText);
//            log.info(encrypted);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            fail("SHA256Base64Test 처리 오류");
//        }
//    }
//
//    @Test
//    @DisplayName("TEST CASE: SHA256/Hex")
//    public void SHA256HexTest() {
//        try {
//            String encrypted = Crypto.encryptionWithSHA256ToHex(plainText);
//            log.info(encrypted);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            fail("SHA256HexTest 처리 오류");
//        }
//    }

//    @Test
//    @DisplayName("TEST CASE: SHA512/Hex/With Salt")
//    public void SHA512HexWithSalt() {
//        try {
//            byte[] encrypted = Crypto.encryptionWithSHA(plainText, Crypto.SHA_512, Crypto.generateBase64Salt(16));
//            String hexStr = DatatypeConverter.printHexBinary(encrypted);
//            log.info(hexStr);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            fail("SHA512HexWithSalt 처리 오류");
//        }
//    }

//    @Test
//    @DisplayName("TEST CASE: AES/Hex")
//    public void AESTest() {
//        try {
//            String encrypted = Crypto.encryptionWithAESToHex(plainText, key);
//            log.info("AES Encrypted: " + encrypted);
//            String decrypted = Crypto.decryptionWithHexAES(encrypted, key);
//            log.info("AES Decrypted: " + decrypted);
//            assertEquals(plainText, decrypted);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            fail("AESTest 처리 오류");
//        }
//    }
//
//    @Test
//    @DisplayName("TEST CASE: ARIA/Base64")
//    public void ARIATest() {
//        try {
//            String encrypted = Crypto.encryptionWithARIAToBase64(plainText, key);
//            log.info("ARIA Encrypted: " + encrypted);
//            String decrypted = Crypto.decryptionWithBase64ARIA(encrypted, key);
//            log.info("ARIA Decrypted: " + decrypted);
//            assertEquals(plainText, decrypted);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            fail("ARIATest 처리 오류");
//        }
//    }
}

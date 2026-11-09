package com.mobigen.framework.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.KeyPair;

import org.junit.jupiter.api.Test;

public class RSATests {
    @Test
    public void testRSA() {
        RSA rsa = new RSA();
        KeyPair keyPair = rsa.getKeyPair();

        String plainText = "hello! world";
        String encrypted = rsa.encryptRSA(plainText, keyPair.getPublic());
        String decrypted = rsa.decryptRSA(encrypted, keyPair.getPrivate());

        String con = String.format(
                "RSA Test ==========\n  - Plain [%s]\n  - Public-Key [%s]\n  - Private-Key [%s]\n  - Encrypted [%s]\n  - Decrypted [%s]",
                plainText, keyPair.getPublic().toString(), keyPair.getPrivate().toString(), encrypted, decrypted);

        System.out.print(con);

        assertEquals(plainText, decrypted);
    }
}

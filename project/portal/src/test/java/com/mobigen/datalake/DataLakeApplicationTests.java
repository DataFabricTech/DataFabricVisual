package com.mobigen.datalake;

import com.mobigen.framework.iris.IRISProperties;
import com.mobigen.framework.iris.Token;
import com.mobigen.framework.test.AbstractRestDocTest;
import com.mobigen.framework.utility.AESGCMEncryptor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

class DataLakeApplicationTests extends AbstractRestDocTest {
    @Value("${properties.cryptKey}")
    private String SECRET_KEY;

    @Autowired
    private Token token;

    @Autowired
    private IRISProperties properties;

    @Test
    void testGetUser() throws Exception {
        // given
        System.out.println("test");
    }

    // 설정파일 암호화
    @Test
    void testEnc() throws Exception {
        // 암호화
        StringEncryptor encryptor = stringEncryptor();

        String postgresPassword = "<PASSWORD>";
        String encryptedString = encryptor.encrypt(postgresPassword);
        System.out.println("=========== 암호화 내역 ====================");
        System.out.println("postgres password : " + encryptedString);
        System.out.println("==========================================");

        // 복호화가 필요한 경우 다음과 같이 사용
        String decryptedString = encryptor.decrypt(encryptedString);
        System.out.println("=========== 복호화 내역 ====================");
        System.out.println("postgres password : " + decryptedString);
        System.out.println("==========================================");
    }

    // 암호화 메서드
    public StringEncryptor stringEncryptor() throws Exception {
        return new AESGCMEncryptor(SECRET_KEY);
    }

    @Test
    public void passwordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
        // 비밀번호 해싱
        String rawPassword = "<PASSWORD>";
        String hashedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Hashed Password: " + hashedPassword);
    
        // 비밀번호 검증
        boolean isMatch = passwordEncoder.matches(rawPassword, hashedPassword);
        System.out.println("Password matches: " + isMatch);
    }
}

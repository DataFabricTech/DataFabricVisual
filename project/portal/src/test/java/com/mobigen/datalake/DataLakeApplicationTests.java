package com.mobigen.datalake;

import com.mobigen.framework.iris.IRISProperties;
import com.mobigen.framework.iris.Token;
import com.mobigen.framework.test.AbstractRestDocTest;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import jakarta.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

class DataLakeApplicationTests extends AbstractRestDocTest {
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
    void testEnc(){
        // 암호화
        String postgresUsername = "rotem";
        String postgresPassword = "<PASSWORD>";

        String keystorePassword = "<PASSWORD>";
        
        String atlasUsername = "admin";
        String atlasPassword = "<PASSWORD>";

        String trinoUsername = "<USER>";

        String superAdminPassword = "<PASSWORD>";

        String tempuserPasswordList = "<PASSWORD>";
        System.out.println("=========== 암호화 내역 ====================");
        System.out.println("postgres username : " + stringEncryptor().encrypt(postgresUsername));
        System.out.println("postgres password : " + stringEncryptor().encrypt(postgresPassword));
        System.out.println("keystore password : " + stringEncryptor().encrypt(keystorePassword));
        System.out.println("atlas username : " + stringEncryptor().encrypt(atlasUsername));
        System.out.println("atlas password : " + stringEncryptor().encrypt(atlasPassword));
        System.out.println("trino username : " + stringEncryptor().encrypt(trinoUsername));
        System.out.println("SuperAdmin password : " + stringEncryptor().encrypt(superAdminPassword));
        System.out.println("dpadmin password : " + stringEncryptor().encrypt(superAdminPassword));
        System.out.println("dpuser01 password : " + stringEncryptor().encrypt(superAdminPassword));
        System.out.println("dpuser02 password : " + stringEncryptor().encrypt(superAdminPassword));
        System.out.println("tempuser passwordList : " + stringEncryptor().encrypt(tempuserPasswordList));
        System.out.println("==========================================");

        // 복호화가 필요한 경우 다음과 같이 사용
        // String encValue = "value";
        // System.out.println(stringEncryptor().decrypt(encValue));
    }

    // 암호화 메서드
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("datalakeEnc");
        config.setAlgorithm("PBEWithMD5AndDES"); //사용할 알고리즘
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        encryptor.setConfig(config);
        return encryptor;
    }
}

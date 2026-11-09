package com.mobigen.framework.configuration;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mobigen.framework.utility.AESGCMEncryptor;

@Configuration
public class JasyptConfig {
    @Value("${properties.cryptKey}")
    private String SECRET_KEY;

    // 서버와 공통으로 사용하는 암호화 AES GCM Nopadding
    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() throws Exception {
        return new AESGCMEncryptor(SECRET_KEY);
    }
}
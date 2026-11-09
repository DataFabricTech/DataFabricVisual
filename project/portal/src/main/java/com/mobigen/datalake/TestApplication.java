// package com.mobigen.datalake;

// import lombok.extern.slf4j.Slf4j;
// import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
// import org.springframework.context.annotation.ComponentScan;

// @ComponentScan(basePackages = {"com"})
// @SpringBootApplication
// @Slf4j
// public class TestApplication extends SpringBootServletInitializer {


//     public static void main(String[] args) {
//         log.info(jasyptEncoding("iwdf.12#"));  //암호화할 내용 입력
//     }

//     public static String jasyptEncoding(String value) {

//         String key = "datalakeEnc";
//         StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
//         pbeEnc.setAlgorithm("PBEWithMD5AndDES");
//         pbeEnc.setPassword(key);
//         return pbeEnc.encrypt(value);
//     }

// }

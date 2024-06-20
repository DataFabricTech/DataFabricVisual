package com.mobigen.datafabric;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableCaching
@EnableAsync
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com"})
@ServletComponentScan
@EnableJpaAuditing
//@SpringBootApplication
// TODO: [NOTE] IWDF 미적용으로 인한 Security 설정 비활성화 -> 추후 IWDF 적용 시 exclude 옵션 삭제 필요
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class DataFabricApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataFabricApplication.class, args);
    }

}

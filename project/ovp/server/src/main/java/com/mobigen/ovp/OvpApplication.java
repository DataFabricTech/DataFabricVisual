package com.mobigen.ovp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableFeignClients
@EnableCaching
@EnableAsync
@EnableAspectJAutoProxy
@ServletComponentScan
@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = {"com"})
public class OvpApplication {

    public static void main(String[] args) {
        SpringApplication.run(OvpApplication.class, args);
    }
}

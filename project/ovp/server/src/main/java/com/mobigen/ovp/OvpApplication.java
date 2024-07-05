package com.mobigen.ovp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableCaching
@EnableAsync
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com"})
@ServletComponentScan
@SpringBootApplication
@EnableFeignClients
public class OvpApplication {

    public static void main(String[] args) {
        SpringApplication.run(OvpApplication.class, args);
    }

}

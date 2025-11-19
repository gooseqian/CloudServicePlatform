package com.example.cloudserviceplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.example.cloudserviceplatform.dao")
@EnableAspectJAutoProxy
public class CloudServicePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudServicePlatformApplication.class, args);
    }

}

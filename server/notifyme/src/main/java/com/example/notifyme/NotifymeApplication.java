package com.example.notifyme;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.temp.notifyme.mapper")
public class NotifymeApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotifymeApplication.class, args);
    }

}

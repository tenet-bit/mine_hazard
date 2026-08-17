package com.mine.hazard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mine.hazard.mapper")
public class MineHazardApplication {

    public static void main(String[] args) {
        SpringApplication.run(MineHazardApplication.class, args);
    }
}

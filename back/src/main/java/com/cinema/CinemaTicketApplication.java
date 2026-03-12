package com.cinema;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.cinema.mapper")
@EnableScheduling
public class CinemaTicketApplication {
    public static void main(String[] args) {
        SpringApplication.run(CinemaTicketApplication.class, args);
    }
}

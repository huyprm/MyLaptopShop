package com.ptithcm2021.laptopshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LaptopShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaptopShopApplication.class, args);
    }

}

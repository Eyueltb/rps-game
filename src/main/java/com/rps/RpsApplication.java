package com.rps;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "rps Game", version = "1.0"))
public class RpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpsApplication.class, args);
    }

}

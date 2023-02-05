package com.example.camel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.camel")
public class CamelRabbitMqExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamelRabbitMqExampleApplication.class, args);
	}

}

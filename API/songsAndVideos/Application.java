package com.example._4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example._4") // 패키지 전체를 검색
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

package com.studymate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//비동기활성화 -> 이게 있어야 spring이 @Async를 보고 별도 스레드에서 실행시킴
@EnableAsync
public class StudymateApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudymateApplication.class, args);
	}
}

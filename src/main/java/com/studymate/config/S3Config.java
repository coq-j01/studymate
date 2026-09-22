package com.studymate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {
	@Value("${aws.region}") //properties에 있는 값을 가져옴
	private String region;

	@Bean
	public S3Client s3Client() { //Java 프로그램이 AWS S3한테 명령을 보내는 객체
		return S3Client.builder().region(Region.of(region)) //이 S3Client는 서울 리전에 연결해
				.build(); 
	}
	
//	private S3 파일을
//	일정 시간 동안 브라우저가 볼 수 있는 URL로 만들어줌
	@Bean
	public S3Presigner s3Presigner() {
		return S3Presigner.builder()
	            .region(Region.of(region))
	            .build();
	}
}

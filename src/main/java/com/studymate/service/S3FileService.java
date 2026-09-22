package com.studymate.service;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
public class S3FileService {
	private final S3Client s3Client;
	private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public S3FileService(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    public String uploadImage(MultipartFile file) {

        // 1. 파일 검증
    	if(file == null || file.isEmpty()) {
    		throw new IllegalArgumentException("업로드할 이미지가 없습니다.");
    	}
    	String contentType = file.getContentType();

    	if (contentType == null || !contentType.startsWith("image/")) {
    	    throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
    	}

        // 2. 확장자 구하기
    	String originalFilename = file.getOriginalFilename();
    	if (originalFilename == null ||
    	        !originalFilename.contains(".")) {

    	    throw new IllegalArgumentException(
    	        "파일 확장자를 확인할 수 없습니다."
    	    );
    	}
    	
    	String extension =
    	        originalFilename.substring(
    	                originalFilename.lastIndexOf(".")
    	        ); //png, jpg 등 확장자 구하기
    	
        // 3. UUID 파일명 생성
    	String savedFilename =
    	        UUID.randomUUID() + extension;

        // 4. object key 생성 -> study/UUID.png
    	String objectKey ="study/" + savedFilename;
    	
        // 5. S3 업로드
    	PutObjectRequest putObjectRequest = PutObjectRequest.builder()
    					.bucket(bucket)
    	                .key(objectKey)
    	                .contentType(contentType)
    	                .build();
    	
    	try {
    	    s3Client.putObject(
    	        putObjectRequest, //저장위치
    	        RequestBody.fromInputStream( //저장 이미지
    	            file.getInputStream(),
    	            file.getSize()
    	        )
    	    );
    	} catch (IOException e) {
    	    throw new IllegalStateException(
    	        "이미지 업로드에 실패했습니다.",
    	        e
    	    );
    	}
    	
        // 6. object key 반환
    	return objectKey;
    }
    public String getImageUrl(String objectKey) {
    	//조회하고 싶은 파일
    	GetObjectRequest getObjectRequest =
                GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(objectKey)
                        .build();
    	
    	//파일을 10분 동안 접근 가능하게 해주는 URL을 만들어줌
        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .getObjectRequest(getObjectRequest)
                        .build();

        //url 생성
        PresignedGetObjectRequest presignedRequest =
                s3Presigner.presignGetObject(presignRequest);
        //url string으로 반환
        return presignedRequest.url().toString();
    }
    public void deleteImage(String objectKey) {
    	if (objectKey == null || objectKey.isBlank()) {
            return;
        }

        // 기본 이미지는 공용 이미지라 삭제하지 않음
        if (objectKey.startsWith("defaults/")) {
            return;
        }
        
    	DeleteObjectRequest deleteObjectRequest =
    			DeleteObjectRequest.builder()
    						.bucket(bucket)
    						.key(objectKey)
    						.build();
    	s3Client.deleteObject(deleteObjectRequest);
    }
}

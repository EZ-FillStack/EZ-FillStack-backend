package com.ezwell.backend.global.infra;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloudflare.r2.bucket-name}")
    private String bucketName;
	
	// https://pub-abe5952db726492dbfcf21b4743e703f.r2.dev = 임시 공개 도메인
    @Value("${cloudflare.r2.endpoint}")
    private String endpoint;
    
    // 조회용 퍼블릭 URL (프론트 반환URL)
    @Value("${cloudflare.r2.public-url}")
    private String publicUrl;
    
        // r2 버킷에 파일 업로드 -> URL 반환
        public String uploadFile(MultipartFile file, String directory) {
            if (file.isEmpty()) {
                throw new RuntimeException("파일이 비어있습니다.");
            }
     
            // 파일명 생성 (중복방지 UUID)
            String fileName = directory + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
     
            try {
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType(file.getContentType())
                        .build();
    		
            // 전송
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
 
            return String.format("%s/%s", publicUrl, fileName);
            
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }
 
    // 파일 삭제
    public void deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }
	
}

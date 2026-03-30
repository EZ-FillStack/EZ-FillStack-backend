package com.ezwell.backend.global.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ezwell.backend.global.infra.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageUploadController {

	private final S3Service s3Service;
	
	@PostMapping("/upload")
	public  ResponseEntity<Map<String, String>> uploadImage(@RequestParam("upload") MultipartFile file){

    	// 이미지 검증
    	String contentType = file.getContentType();
    	
    	if(contentType == null || !contentType.startsWith("image/")) {
    		return ResponseEntity.badRequest().body(Map.of("error", "이미지 파일만 업로드 가능합니다."));
    	}
    	
    	// 사이즈 제한 5MB
    	if(file.getSize() > 5*1024 *1024) {
    		return ResponseEntity.badRequest().body(Map.of("error", "이미지는 5MB 이하여야 합니다."));
    	}
    	
		// 'editor' 디렉토리에 저장
        String fileUrl = s3Service.uploadFile(file, "editor");
        
        log.info("[CKEditor 이미지 업로드] 크기 : {} bytes", file.getSize());
        
		return ResponseEntity.ok(Map.of("url",fileUrl));
	}
}

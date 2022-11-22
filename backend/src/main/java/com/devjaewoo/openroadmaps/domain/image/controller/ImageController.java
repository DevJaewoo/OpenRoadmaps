package com.devjaewoo.openroadmaps.domain.image.controller;

import com.devjaewoo.openroadmaps.domain.image.dto.ImageDto;
import com.devjaewoo.openroadmaps.domain.image.service.ImageService;
import com.devjaewoo.openroadmaps.global.utils.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("image") MultipartFile image) {
        SessionUtil.getCurrentClient();
        String url = imageService.upload(image);
        return ResponseEntity.ok(new ImageDto.UploadResponse(url));
    }

    // 로컬에 저장한 이미지 전용
    @GetMapping("/{name}")
    public ResponseEntity<byte[]> download(@PathVariable String name) {
        ImageDto imageDto = imageService.get(name);
        return ResponseEntity.ok().contentType(imageDto.mediaType()).body(imageDto.image());
    }
}

package com.devjaewoo.openroadmaps.domain.image.service;

import com.devjaewoo.openroadmaps.domain.image.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String upload(MultipartFile image);
    ImageDto get(String url);
}

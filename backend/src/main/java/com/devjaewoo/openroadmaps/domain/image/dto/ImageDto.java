package com.devjaewoo.openroadmaps.domain.image.dto;

import org.springframework.http.MediaType;

public record ImageDto(String url, byte[] image, MediaType mediaType) {
    public record UploadResponse(String url) {}
}

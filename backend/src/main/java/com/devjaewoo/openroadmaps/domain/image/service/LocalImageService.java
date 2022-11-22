package com.devjaewoo.openroadmaps.domain.image.service;

import com.devjaewoo.openroadmaps.domain.image.dto.ImageDto;
import com.devjaewoo.openroadmaps.domain.image.dto.ImageErrorCode;
import com.devjaewoo.openroadmaps.domain.image.utils.ImageUtil;
import com.devjaewoo.openroadmaps.global.exception.CommonErrorCode;
import com.devjaewoo.openroadmaps.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalImageService implements ImageService {

    @Value("${file.rootDir:D:/}")
    private String fileDir;

    private String generateFileName(String imageName) {
        String fileName = ImageUtil.getImageName(imageName);
        String extension = ImageUtil.getImageExtension(imageName);

        if(!ImageUtil.isAllowedExtension(extension)) throw new RestApiException(ImageErrorCode.BAD_EXTENSION);

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        return fileName + "-" + UUID.randomUUID() + "-" + date + "." + extension;
    }

    @Override
    public String upload(MultipartFile image) {
        String fileName = generateFileName(image.getOriginalFilename());
        String filePath = fileDir + fileName;

        try {
            Path path = Path.of(fileDir);
            if(!Files.exists(path)) {
                Files.createDirectory(path);
            }

            FileOutputStream out = new FileOutputStream(filePath);
            out.write(image.getBytes());
            out.close();
        }
        catch (IOException e) {
            log.warn(e.getMessage());
            throw new RestApiException(ImageErrorCode.CREATE_ERROR);
        }

        return fileName;
    }

    @Override
    public ImageDto get(String url) {
        String path = fileDir + url;

        if(!Files.exists(Path.of(path))) {
            throw new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND);
        }

        try {
            FileInputStream in = new FileInputStream(path);
            byte[] image = in.readAllBytes();
            in.close();

            String extension = ImageUtil.getImageExtension(url);
            MediaType mediaType = switch (extension) {
                case "png" -> MediaType.IMAGE_PNG;
                case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
                case "gif" -> MediaType.IMAGE_GIF;
                default -> throw new RestApiException(ImageErrorCode.BAD_EXTENSION);
            };

            return new ImageDto(url, image, mediaType);
        }
        catch (IOException e) {
                log.warn(e.getMessage());
                throw new RestApiException(ImageErrorCode.CREATE_ERROR);
        }
    }
}

package com.devjaewoo.openroadmaps.domain.image.utils;

import java.util.Arrays;

public class ImageUtil {

    public static String getImageExtension(String imageName) {
        if(imageName == null) return "";

        int index = imageName.lastIndexOf(".");
        if(index == -1) return "";

        return imageName.substring(index + 1);
    }

    public static String getImageName(String imageName) {
        if(imageName == null) return "";

        int index = imageName.lastIndexOf(".");
        if(index == -1) return "";

        return imageName.substring(0, index);
    }

    public static boolean isAllowedExtension(String extension) {
        return Arrays.asList("png", "jpg", "jpeg", "gif").contains(extension);
    }
}

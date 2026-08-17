package com.dermavisionai.utils;

import java.io.File;
import java.util.Locale;
import java.util.regex.Pattern;

public final class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final long MAX_IMAGE_BYTES = 8L * 1024L * 1024L;

    private ValidationUtil() {
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isStrongPassword(String password) {
        return password != null && password.length() >= 8;
    }

    public static void validateImage(File file) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("Please choose an image file.");
        }
        String name = file.getName().toLowerCase(Locale.ROOT);
        boolean validExtension = name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
        if (!validExtension) {
            throw new IllegalArgumentException("Only JPG, JPEG, and PNG images are supported.");
        }
        if (file.length() > MAX_IMAGE_BYTES) {
            throw new IllegalArgumentException("Image must be smaller than 8 MB.");
        }
    }
}

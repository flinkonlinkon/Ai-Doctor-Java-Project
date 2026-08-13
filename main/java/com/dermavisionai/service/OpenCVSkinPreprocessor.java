package com.dermavisionai.service;

import java.io.File;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * Placeholder OpenCV component showing where image preprocessing can be attached.
 */
public class OpenCVSkinPreprocessor {
    public boolean canRead(File image) {
        try {
            Mat mat = Imgcodecs.imread(image.getAbsolutePath());
            return mat != null && !mat.empty();
        } catch (UnsatisfiedLinkError | RuntimeException ex) {
            return image != null && image.exists();
        }
    }
}

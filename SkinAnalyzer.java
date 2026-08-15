package com.dermavisionai.service;

import com.dermavisionai.model.SkinAnalysisResult;
import java.io.File;

/**
 * Extension point for replacing the mock analysis with TensorFlow, PyTorch, OpenCV, or an external API.
 */
public interface SkinAnalyzer {
    SkinAnalysisResult analyzeSkin(File image);
}

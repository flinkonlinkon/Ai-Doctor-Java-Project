package com.dermavisionai.service;

import com.dermavisionai.model.Severity;
import com.dermavisionai.model.SkinAnalysisResult;
import com.dermavisionai.model.SkinIssue;
import com.dermavisionai.utils.ValidationUtil;
import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * Deterministic mock analyzer for demos and automated testing.
 */
public class MockSkinAnalyzer implements SkinAnalyzer {
    private static final List<String> ISSUE_NAMES = List.of(
            "Acne", "Pimples", "Wrinkles", "Dark Spots", "Pigmentation",
            "Redness", "Eye Bags", "Oiliness", "Dryness"
    );

    @Override
    public SkinAnalysisResult analyzeSkin(File image) {
        ValidationUtil.validateImage(image);
        Random random = new Random(image.getName().hashCode() + image.length());
        int skinScore = 58 + random.nextInt(35);
        double confidence = 82 + random.nextDouble(14);
        Severity overall = severityFromRisk(100 - skinScore);
        String skinType = List.of("Oily", "Dry", "Combination", "Sensitive", "Normal").get(random.nextInt(5));
        SkinAnalysisResult result = new SkinAnalysisResult(skinScore, confidence, overall, skinType);
        for (String name : ISSUE_NAMES) {
            int score = 15 + random.nextInt(76);
            result.addIssue(new SkinIssue(name, score, 70 + random.nextDouble(25), severityFromRisk(score)));
        }
        return result;
    }

    private Severity severityFromRisk(int risk) {
        if (risk >= 67) {
            return Severity.HIGH;
        }
        if (risk >= 35) {
            return Severity.MEDIUM;
        }
        return Severity.LOW;
    }
}

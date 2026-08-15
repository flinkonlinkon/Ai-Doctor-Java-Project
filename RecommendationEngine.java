package com.dermavisionai.service;

import com.dermavisionai.model.Recommendation;
import com.dermavisionai.model.Severity;
import com.dermavisionai.model.SkinAnalysisResult;
import com.dermavisionai.model.SkinIssue;

public class RecommendationEngine {
    public Recommendation generate(SkinAnalysisResult result) {
        Recommendation recommendation = new Recommendation();
        recommendation.getMorningRoutine().add("Cleanse with a gentle pH-balanced face wash.");
        recommendation.getMorningRoutine().add("Apply a lightweight moisturizer matched to " + result.getSkinType() + " skin.");
        recommendation.getMorningRoutine().add("Use broad-spectrum SPF 30 or higher every morning.");
        recommendation.getNightRoutine().add("Remove sunscreen and makeup before cleansing.");
        recommendation.getNightRoutine().add("Apply treatment products slowly, 2-3 nights per week at first.");
        recommendation.getNightRoutine().add("Seal with a barrier-supporting moisturizer.");
        recommendation.getSuggestedIngredients().add("Niacinamide for barrier support and oil balance.");
        recommendation.getSuggestedIngredients().add("Hyaluronic acid for hydration.");
        recommendation.getAvoidIngredients().add("Harsh physical scrubs.");
        recommendation.getAvoidIngredients().add("Strong fragrance if irritation is present.");
        recommendation.getDailyTips().add("Patch test new products for 24 hours.");
        recommendation.getDailyTips().add("Keep pillowcases and phone screens clean.");
        recommendation.setWaterIntake("2-3 liters per day, adjusted for climate and activity.");
        recommendation.setSleepHours("7-9 hours of consistent sleep.");
        recommendation.setSunProtectionAdvice("Reapply sunscreen every 2-3 hours outdoors and use shade when UV is high.");

        for (SkinIssue issue : result.getIssues()) {
            addIssueAdvice(recommendation, issue);
        }
        return recommendation;
    }

    private void addIssueAdvice(Recommendation recommendation, SkinIssue issue) {
        if (issue.getSeverity() == Severity.LOW) {
            return;
        }
        switch (issue.getName()) {
            case "Acne", "Pimples" -> {
                recommendation.getSuggestedIngredients().add("Salicylic acid or benzoyl peroxide for acne-prone areas.");
                recommendation.getAvoidIngredients().add("Heavy pore-clogging oils on active breakouts.");
            }
            case "Wrinkles" -> recommendation.getSuggestedIngredients().add("Retinoids or peptides at night, introduced gradually.");
            case "Dark Spots", "Pigmentation" -> recommendation.getSuggestedIngredients().add("Vitamin C, azelaic acid, and daily sunscreen for uneven tone.");
            case "Redness" -> {
                recommendation.getSuggestedIngredients().add("Centella asiatica, panthenol, and ceramides for calming.");
                recommendation.getAvoidIngredients().add("Over-exfoliation and alcohol-heavy toners.");
            }
            case "Eye Bags" -> recommendation.getDailyTips().add("Reduce late-night salt intake and use a cool compress for puffiness.");
            case "Oiliness" -> recommendation.getSuggestedIngredients().add("Gel moisturizers and niacinamide for shine control.");
            case "Dryness" -> recommendation.getSuggestedIngredients().add("Ceramides, glycerin, and occlusive moisturizer at night.");
            default -> recommendation.getDailyTips().add("Track changes weekly and avoid changing too many products at once.");
        }
    }
}

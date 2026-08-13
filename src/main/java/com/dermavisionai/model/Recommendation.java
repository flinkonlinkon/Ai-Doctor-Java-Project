package com.dermavisionai.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Personalized skincare recommendation created from analysis output.
 */
public class Recommendation {
    private final List<String> morningRoutine = new ArrayList<>();
    private final List<String> nightRoutine = new ArrayList<>();
    private final List<String> suggestedIngredients = new ArrayList<>();
    private final List<String> avoidIngredients = new ArrayList<>();
    private final List<String> dailyTips = new ArrayList<>();
    private String waterIntake;
    private String sleepHours;
    private String sunProtectionAdvice;

    public List<String> getMorningRoutine() {
        return morningRoutine;
    }

    public List<String> getNightRoutine() {
        return nightRoutine;
    }

    public List<String> getSuggestedIngredients() {
        return suggestedIngredients;
    }

    public List<String> getAvoidIngredients() {
        return avoidIngredients;
    }

    public List<String> getDailyTips() {
        return dailyTips;
    }

    public String getWaterIntake() {
        return waterIntake;
    }

    public void setWaterIntake(String waterIntake) {
        this.waterIntake = waterIntake;
    }

    public String getSleepHours() {
        return sleepHours;
    }

    public void setSleepHours(String sleepHours) {
        this.sleepHours = sleepHours;
    }

    public String getSunProtectionAdvice() {
        return sunProtectionAdvice;
    }

    public void setSunProtectionAdvice(String sunProtectionAdvice) {
        this.sunProtectionAdvice = sunProtectionAdvice;
    }

    public String toReadableText() {
        return "Morning Routine:\n" + format(morningRoutine)
                + "\nNight Routine:\n" + format(nightRoutine)
                + "\nSuggested Ingredients:\n" + format(suggestedIngredients)
                + "\nAvoid Ingredients:\n" + format(avoidIngredients)
                + "\nDaily Tips:\n" + format(dailyTips)
                + "\nWater Intake: " + waterIntake
                + "\nSleep Hours: " + sleepHours
                + "\nSun Protection: " + sunProtectionAdvice;
    }

    private String format(List<String> items) {
        StringBuilder builder = new StringBuilder();
        for (String item : items) {
            builder.append("- ").append(item).append("\n");
        }
        return builder.toString();
    }
}

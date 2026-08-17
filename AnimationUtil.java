package com.dermavisionai.utils;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public final class AnimationUtil {
    private AnimationUtil() {
    }

    public static void fadeIn(Node node) {
        FadeTransition transition = new FadeTransition(Duration.millis(220), node);
        transition.setFromValue(0.2);
        transition.setToValue(1.0);
        transition.play();
    }
}

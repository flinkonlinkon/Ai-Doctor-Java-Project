package com.dermavisionai.utils;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public final class ViewLoader {
    private ViewLoader() {
    }

    public static Parent load(String resourcePath) {
        URL resource = ViewLoader.class.getResource(resourcePath);
        if (resource == null) {
            throw new IllegalStateException("FXML not found: " + resourcePath);
        }
        try {
            return FXMLLoader.load(resource);
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to load view: " + resourcePath, ex);
        }
    }
}

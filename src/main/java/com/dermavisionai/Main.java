package com.dermavisionai;

import com.dermavisionai.database.DatabaseInitializer;
import com.dermavisionai.utils.ViewLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX entry point for DermaVision AI.
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) {
        DatabaseInitializer.initialize();
        Scene scene = new Scene(ViewLoader.load("/fxml/login.fxml"), 1100, 720);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        stage.setTitle("DermaVision AI");
        stage.setMinWidth(960);
        stage.setMinHeight(640);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

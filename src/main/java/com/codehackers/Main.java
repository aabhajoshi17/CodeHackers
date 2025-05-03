package com.codehackers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Starting application...");
        
        URL fxmlUrl = getClass().getResource("/course-view.fxml");
        if (fxmlUrl == null) {
            System.err.println("FXML file not found!");
            System.err.println("Current classpath: " + System.getProperty("java.class.path"));
            System.exit(1);
        }
Parent root = FXMLLoader.load(fxmlUrl);
        stage.setTitle("Course Recommender");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public static void main(String[] args) {
        System.out.println("Launching JavaFX application");
        launch(args);
    }
}
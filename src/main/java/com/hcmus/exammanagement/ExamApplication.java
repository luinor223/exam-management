package com.hcmus.exammanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class ExamApplication extends Application {

    private AutoTaskScheduler autoTaskScheduler;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hcmus/exammanagement/login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/hcmus/exammanagement/style.css")).toExternalForm());

        stage.setTitle("Assessify");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (autoTaskScheduler != null) {
            autoTaskScheduler.stop();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

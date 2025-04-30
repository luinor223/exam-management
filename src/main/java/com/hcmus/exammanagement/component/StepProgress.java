package com.hcmus.exammanagement.component;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class StepProgress extends HBox {
    private final Color ACTIVE_COLOR = Color.web("#18c27b");
    private final Color INACTIVE_COLOR = Color.web("#d3d3d3");
    private final Color ACTIVE_TEXT_COLOR = Color.web("#18c27b");
    private final Color INACTIVE_TEXT_COLOR = Color.web("#757575");

    private final List<Circle> stepCircles = new ArrayList<>();
    private final List<Label> stepLabels = new ArrayList<>();
    private final List<Line> connectorLines = new ArrayList<>();
    @Getter
    private int currentStep = 1;
    @Getter
    private int totalSteps = 0;

    public StepProgress() {
        this.setAlignment(javafx.geometry.Pos.CENTER);
        this.setSpacing(10);
    }

    public void configureSteps(List<String> labels) {
        this.getChildren().clear();
        stepCircles.clear();
        stepLabels.clear();
        connectorLines.clear();

        totalSteps = labels.size();

        for (int i = 0; i < totalSteps; i++) {
            Circle circle = new Circle(15.0);
            circle.setStroke(Color.WHITE);
            circle.setStrokeWidth(2.0);

            Label label = new Label(labels.get(i));

            VBox stepBox = new VBox();
            stepBox.setAlignment(javafx.geometry.Pos.CENTER);
            stepBox.getChildren().addAll(circle, label);
            HBox.setHgrow(stepBox, javafx.scene.layout.Priority.ALWAYS);

            this.getChildren().add(stepBox);
            stepCircles.add(circle);
            stepLabels.add(label);

            if (i < totalSteps - 1) {
                Line line = new Line();
                line.setEndX(100.0);
                line.setStrokeWidth(2.0);
                this.getChildren().add(line);
                connectorLines.add(line);
            }
        }

        setCurrentStep(1);
    }

    public void setCurrentStep(int step) {
        if (step < 1 || step > totalSteps) {
            step = 1;
        }

        currentStep = step;

        for (int i = 0; i < totalSteps; i++) {
            if (i < step) {
                stepCircles.get(i).setFill(ACTIVE_COLOR);
                stepLabels.get(i).setTextFill(ACTIVE_TEXT_COLOR);

                if (i > 0) {
                    connectorLines.get(i-1).setStroke(ACTIVE_COLOR);
                }
            } else {
                stepCircles.get(i).setFill(INACTIVE_COLOR);
                stepLabels.get(i).setTextFill(INACTIVE_TEXT_COLOR);

                connectorLines.get(i - 1).setStroke(INACTIVE_COLOR);
            }
        }
    }

}
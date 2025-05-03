package com.codehackers.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.codehackers.model.Course;
import com.codehackers.CourseRecommender;

public class CourseController {
    @FXML private TextField nameField;
    @FXML private TextField completedCoursesField;
    @FXML private TextField interestField;
    @FXML private Button recommendButton;
    @FXML private TableView<Course> coursesTable;
    @FXML private TableColumn<Course, String> titleColumn;
    @FXML private TableColumn<Course, String> orgColumn;
    @FXML private TableColumn<Course, Double> ratingColumn;
    @FXML private TableColumn<Course, String> difficultyColumn;
    @FXML private TableColumn<Course, String> enrolledColumn;
    @FXML private Label statusLabel;

    private final CourseRecommender recommender = new CourseRecommender();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        orgColumn.setCellValueFactory(new PropertyValueFactory<>("organization"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        enrolledColumn.setCellValueFactory(new PropertyValueFactory<>("studentsEnrolled"));

        ratingColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double rating, boolean empty) {
                super.updateItem(rating, empty);
                setText(empty || rating == null ? null : String.format("%.1f", rating));
            }
        });
    }

    @FXML
    private void handleRecommend() {
        String name = nameField.getText();
        String completedCourses = completedCoursesField.getText();
        String interest = interestField.getText();

        if (name.isEmpty() || completedCourses.isEmpty() || interest.isEmpty()) {
            statusLabel.setText("Please fill in all fields!");
            return;
        }

        try {
            ObservableList<Course> recommendedCourses = recommender.recommendCourses(name, completedCourses, interest);
            coursesTable.setItems(recommendedCourses);
            statusLabel.setText(recommendedCourses.isEmpty() ? 
                "No courses found matching your criteria." : 
                "Found " + recommendedCourses.size() + " recommended courses.");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
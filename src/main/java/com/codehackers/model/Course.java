package com.codehackers.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Course {
    private final SimpleStringProperty title;
    private final SimpleStringProperty organization;
    private final SimpleDoubleProperty rating;
    private final SimpleStringProperty difficulty;
    private final SimpleStringProperty studentsEnrolled;

    public Course(String title, String organization, double rating, 
                 String difficulty, String studentsEnrolled) {
        this.title = new SimpleStringProperty(title);
        this.organization = new SimpleStringProperty(organization);
        this.rating = new SimpleDoubleProperty(rating);
        this.difficulty = new SimpleStringProperty(difficulty);
        this.studentsEnrolled = new SimpleStringProperty(studentsEnrolled);
    }

    // Getters
    public String getTitle() { return title.get(); }
    public String getOrganization() { return organization.get(); }
    public double getRating() { return rating.get(); }
    public String getDifficulty() { return difficulty.get(); }
    public String getStudentsEnrolled() { return studentsEnrolled.get(); }

    // Property getters
    public SimpleStringProperty titleProperty() { return title; }
    public SimpleStringProperty organizationProperty() { return organization; }
    public SimpleDoubleProperty ratingProperty() { return rating; }
    public SimpleStringProperty difficultyProperty() { return difficulty; }
    public SimpleStringProperty studentsEnrolledProperty() { return studentsEnrolled; }
}
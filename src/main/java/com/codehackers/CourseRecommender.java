package com.codehackers;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import com.codehackers.model.Course;
import java.sql.*;
import java.util.*;

public class CourseRecommender {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/courses_db";
    private static final String USER = "root";
    private static final String PASS = "Aabha@1705";

    public ObservableList<Course> recommendCourses(String name, String done, String interest) throws SQLException {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            List<String> completedCourses = Arrays.asList(done.split(","));
            StringBuilder placeholders = new StringBuilder();
            for (int i = 0; i < completedCourses.size(); i++) {
                placeholders.append(i == 0 ? "?" : ",?");
            }

            String sql = "SELECT course_title, course_organization, course_rating, " +
                         "course_difficulty, course_students_enrolled " +
                         "FROM coursea_data " +
                         "WHERE course_title LIKE ? " +
                         (completedCourses.isEmpty() ? "" : "AND course_title IN (" + placeholders + ") ") +
                         "AND course_rating >= 4.0 " +
                         "ORDER BY course_rating DESC, " +
                         "CAST(REPLACE(course_students_enrolled,'k','000') AS UNSIGNED) DESC " +
                         "LIMIT 10";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, "%" + interest + "%");
                for (int i = 0; i < completedCourses.size(); i++) {
                    pst.setString(i + 2, completedCourses.get(i).trim());
                }

                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        courses.add(new Course(
                            rs.getString("course_title"),
                            rs.getString("course_organization"),
                            rs.getDouble("course_rating"),
                            rs.getString("course_difficulty"),
                            rs.getString("course_students_enrolled")
                        ));
                    }
                }
            }

            if (courses.isEmpty()) {
                String interestOnlySQL = sql.replace(
                    "AND course_title IN (" + placeholders + ") ", 
                    ""
                );
                try (PreparedStatement pst = conn.prepareStatement(interestOnlySQL)) {
                    pst.setString(1, "%" + interest + "%");
                    try (ResultSet rs = pst.executeQuery()) {
                        while (rs.next()) {
                            courses.add(new Course(
                                rs.getString("course_title"),
                                rs.getString("course_organization"),
                                rs.getDouble("course_rating"),
                                rs.getString("course_difficulty"),
                                rs.getString("course_students_enrolled")
                            ));
                        }
                    }
                }
            }
        }
        return courses;
    }
}
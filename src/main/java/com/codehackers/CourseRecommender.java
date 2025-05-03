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
        Set<Course> uniqueCourses = new LinkedHashSet<>();
        List<String> completedCourses = Arrays.asList(done.split("\\s*,\\s*"));
        
        System.out.println("\n[INPUT] Completed Courses: " + completedCourses);
        System.out.println("[INPUT] Interest: " + interest);

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // Primary Query with both interest and completed courses
            String sql = buildQuery(completedCourses);
            printDebugQuery(sql, interest, completedCourses);

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                bindParameters(pst, interest, completedCourses);
                
                try (ResultSet rs = pst.executeQuery()) {
                    processResults(uniqueCourses, rs);
                }
            }

            // Fallback to interest-only if no results
            if (uniqueCourses.isEmpty()) {
                System.out.println("[STATUS] Trying interest-only fallback");
                String fallbackSql = buildFallbackQuery();
                printDebugQuery(fallbackSql, interest, Collections.emptyList());
                
                try (PreparedStatement pst = conn.prepareStatement(fallbackSql)) {
                    pst.setString(1, "%" + interest + "%");
                    try (ResultSet rs = pst.executeQuery()) {
                        processResults(uniqueCourses, rs);
                    }
                }
            }
        }
        
        System.out.println("[RESULT] Returning " + uniqueCourses.size() + " unique courses");
        return FXCollections.observableArrayList(uniqueCourses);
    }

    private String buildQuery(List<String> completedCourses) {
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < completedCourses.size(); i++) {
            placeholders.append(i == 0 ? "?" : ",?");
        }

        return "SELECT DISTINCT course_title, course_organization, course_rating, " +
               "course_difficulty, course_students_enrolled " +
               "FROM coursea_data " +
               "WHERE course_title LIKE ? " +
               (completedCourses.isEmpty() ? "" : "AND course_title IN (" + placeholders + ") ") +
               "AND course_rating >= 4.0 " +
               "ORDER BY course_rating DESC, " +
               "CAST(REPLACE(course_students_enrolled,'k','000') AS UNSIGNED) DESC " +
               "LIMIT 10";
    }

    private String buildFallbackQuery() {
        return "SELECT DISTINCT course_title, course_organization, course_rating, " +
               "course_difficulty, course_students_enrolled " +
               "FROM coursea_data " +
               "WHERE course_title LIKE ? " +
               "AND course_rating >= 4.0 " +
               "ORDER BY course_rating DESC, " +
               "CAST(REPLACE(course_students_enrolled,'k','000') AS UNSIGNED) DESC " +
               "LIMIT 10";
    }

    private void bindParameters(PreparedStatement pst, String interest, 
                              List<String> completedCourses) throws SQLException {
        pst.setString(1, "%" + interest + "%");
        for (int i = 0; i < completedCourses.size(); i++) {
            pst.setString(i + 2, completedCourses.get(i));
        }
    }

    private void processResults(Set<Course> uniqueCourses, ResultSet rs) throws SQLException {
        while (rs.next()) {
            Course course = new Course(
                rs.getString("course_title"),
                rs.getString("course_organization"),
                rs.getDouble("course_rating"),
                rs.getString("course_difficulty"),
                rs.getString("course_students_enrolled")
            );
            System.out.println("[DATA] Found: " + course.getTitle() + " | " + course.getOrganization());
            uniqueCourses.add(course);
        }
    }

    private void printDebugQuery(String sql, String interest, List<String> completedCourses) {
        String debugSQL = sql;
        // Replace first parameter (LIKE)
        debugSQL = debugSQL.replaceFirst("\\?", "'%" + interest + "%'");
        // Replace IN clause parameters
        for (String course : completedCourses) {
            debugSQL = debugSQL.replaceFirst("\\?", "'" + course + "'");
        }
        System.out.println("[QUERY] " + debugSQL);
    }
}
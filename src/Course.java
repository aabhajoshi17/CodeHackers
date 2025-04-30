package src;
import java.util.*;
import java.sql.*;

public class Course {
    static final String DB_URL  = "jdbc:mysql://localhost:3306/courses_db";
    static final String USER    = "root";
    static final String PASS    = "Aabha@1705";

    public static void main(String[] args) throws SQLException {
        System.out.println("WELCOME TO COURSE RECOMMENDER!!!!");

        try (Scanner sc = new Scanner(System.in)) {
            int srn = 1;

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                System.out.print("Enter your name: ");
                String name = sc.nextLine();

                System.out.print("Enter courses you have completed (comma-separated): ");
                String done = sc.nextLine();

                System.out.print("Enter your area of interest: ");
                String interest = sc.nextLine();

                System.out.println("\nHello " + name + "!");
                System.out.println("Completed: " + done);
                System.out.println("Interest: " + interest);

                // Use HashMap to store completed courses with dummy values
                HashMap<String, Boolean> completedMap = new HashMap<>();
                List<String> completedCourses = Arrays.asList(done.split(","));
                for (String course : completedCourses) {
                    completedMap.put(course.trim(), true);
                }

                // Create placeholders for SQL IN clause
                StringBuilder placeholders = new StringBuilder();
                for (int i = 0; i < completedCourses.size(); i++) {
                    placeholders.append("?");
                    if (i < completedCourses.size() - 1) placeholders.append(",");
                }

                // First SQL: match interest AND completed courses with rating filter
                String sql =
                    "SELECT course_title, course_organization, course_rating, course_difficulty, course_students_enrolled " +
                    "FROM coursea_data " +
                    "WHERE course_title LIKE ? " +
                    "AND course_title IN (" + placeholders.toString() + ") " +
                    "AND course_rating >= 4.0 " +
                    "ORDER BY course_rating DESC, " +
                    "CAST(REPLACE(course_students_enrolled,'k','000') AS UNSIGNED) DESC " +
                    "LIMIT 10";

                try (PreparedStatement pst = conn.prepareStatement(sql)) {
                    pst.setString(1, "%" + interest + "%");
                    for (int i = 0; i < completedCourses.size(); i++) {
                        pst.setString(i + 2, completedCourses.get(i).trim());
                    }

                    System.out.println("\n=== Top 10 Recommended Courses Based on Completed & Interest (Rating ≥ 4.0) ===");
                    try (ResultSet rs = pst.executeQuery()) {
                        boolean found = false;
                        while (rs.next()) {
                            found = true;
                            printCourse(rs, srn++);
                        }
                        if (!found) {
                            System.out.println("No matching courses found. Showing top interest-based courses:");

                            // Second SQL: match only by interest with rating filter
                            String interestOnlySQL =
                                "SELECT course_title, course_organization, course_rating, course_difficulty, course_students_enrolled " +
                                "FROM coursea_data " +
                                "WHERE course_title LIKE ? " +
                                "AND course_rating >= 4.0 " +
                                "ORDER BY course_rating DESC, " +
                                "CAST(REPLACE(course_students_enrolled,'k','000') AS UNSIGNED) DESC " +
                                "LIMIT 10";

                            try (PreparedStatement pst2 = conn.prepareStatement(interestOnlySQL)) {
                                pst2.setString(1, "%" + interest + "%");

                                try (ResultSet rs2 = pst2.executeQuery()) {
                                    boolean any = false;
                                    while (rs2.next()) {
                                        any = true;
                                        printCourse(rs2, srn++);
                                    }
                                    if (!any) {
                                        System.out.println("Still no courses found with your interest and rating criteria.");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    static void printCourse(ResultSet rs, int srn) throws SQLException {
        String t = rs.getString("course_title");
        String org = rs.getString("course_organization");
        double rate = rs.getDouble("course_rating");
        String diff = rs.getString("course_difficulty");
        String enrolled = rs.getString("course_students_enrolled");

        System.out.printf(" %d. %s | %s | Rating: %.1f | Enrolled: %s | Difficulty: %s%n",
                    srn, t, org, rate, enrolled, diff);
        System.out.println("-----------------------------------------");
    }
}

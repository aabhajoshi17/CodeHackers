

import java.sql.*;
import java.util.*;

public class courserecommender {
    public static void main(String[] args) {
        System.out.println("WELCOME TO COURSE RECOMMENDER!\n");

        Scanner sc = new Scanner(System.in);

        try {
            // Take user input
            System.out.println("Enter your name:");
            String name = sc.nextLine(); // Handles spaces

            System.out.println("Enter the courses you have already completed (comma-separated):");
            String completedCourses = sc.nextLine();  

            System.out.println("Enter your area of interest:");
            String interest = sc.nextLine();

            System.out.println("\n--- User Details ---");
            System.out.printf("Name: %s\n", name);
            System.out.printf("Completed Course(s): %s\n", completedCourses);
            System.out.printf("Interest: %s\n", interest);

            // Connect to the database
            String url = "jdbc:mysql://localhost:3306/codesprint"; 
            String user = "root"; 
            String password = "@Sanskruti22"; 

            Connection conn = DriverManager.getConnection(url, user, password);

            // Query to find courses matching the user's interest
            String query = "SELECT title, organization, rating, num_users " + 
                           "FROM courses2 " + 
                           "WHERE course_title LIKE ? " + 
                           "ORDER BY rating DESC, enrolled_students DESC LIMIT 10";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "%" + interest + "%"); // Wildcard search for interest

            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n--- Recommended Courses based on your Interest ---");
            System.out.println("---------------------------------------------------");
            boolean found = false;

            while (rs.next()) {
                String course = rs.getString("course");
                String organization = rs.getString("organization");
                double rating = rs.getDouble("rating");
                int enrolled_students = rs.getInt("enrolled_students");

                // Printing each course in a formatted manner
                System.out.printf("%-40s | %-20s | Rating: %-4.2f | Users: %-5d\n", 
                                  course, organization, rating, enrolled_students);
                found = true;
            }

            if (!found) {
                System.out.println("Sorry! No courses found matching your interest.");
            }

            // Close resources
            rs.close();
            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sc.close(); // Close the scanner
        }
    }
}

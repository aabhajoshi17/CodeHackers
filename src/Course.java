package src;
import java.util.*;
import java.sql.*;

class course {
    public static void main(String[] args) {
        System.out.println("WELCOME TO COURSE RECOMMENDER!!!!");
        Scanner sc = new Scanner(System.in);

        try {
            System.out.println("Enter your name:");
            String name = sc.nextLine(); // Handles spaces

            System.out.println("Enter the courses already done (from the following database):");
            String courses = sc.nextLine();  

            System.out.println("Enter your area of interest:");
            String interest = sc.nextLine();

            System.out.println("Printing your details >>>>");
            System.out.println("Name: " + name);
            System.out.println("Completed Course(s): " + courses);
            System.out.println("Interest: " + interest);

            String url = "jdbc:mysql://localhost:3306/courses_db"; 
            String user = "root"; 
            String password = "Aabha@1705"; 

            Connection conn = DriverManager.getConnection(url, user, password);

            // SQL query to find matching courses based on interest
            String query = "SELECT course_title FROM coursea_data WHERE course_title LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "%" + interest + "%"); // Wildcards for partial matching

            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n--- Recommended Courses based on your interest ---");
            boolean found = false;
            while (rs.next()) {
                String courseTitle = rs.getString("course_title");
                System.out.println(courseTitle);
                found = true;
            }

            if (!found) {
                System.out.println("Sorry! No courses found matching your interest.");
            }

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

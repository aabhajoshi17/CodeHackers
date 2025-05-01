package src;
import java.util.*;
import java.sql.*;

public class courserecommender {
    // JDBC connection constants:
    static final String DB_URL  = "jdbc:mysql://localhost:3306/codesprint";
    static final String USER    = "root";
    static final String PASS    = "@Sanskruti22";  // your actual password

    public static void main(String[] args) {
        System.out.println("WELCOME TO COURSE RECOMMENDER!!!!");
        Scanner sc = new Scanner(System.in);
        int srn=1;

        try (
            // 1) Connect to the database
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        ) {
            // 2) Read user profile
            System.out.print("Enter your name: ");
            String name = sc.nextLine();

            System.out.print("Enter courses you have completed (comma-separated): ");
            String done = sc.nextLine();

            System.out.print("Enter your area of interest: ");
            String interest = sc.nextLine();

            System.out.println("\nHello " + name + "!");
            System.out.println("Completed: " + done);
            System.out.println("Interest: " + interest);

            // 3) Prepare recommendation query
            String sql = 
              "SELECT title, organization, rating, difficulty, enrolled_students " +
              "FROM courses2 " +
              "WHERE title LIKE ? " +
              "ORDER BY rating DESC, " +
                       "CAST(REPLACE(enrolled_students,'k','000') AS UNSIGNED) DESC";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + interest + "%");

            // 4) Execute and print results
            System.out.println("\n=== Recommended Courses ===");
            try (ResultSet rs = pst.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    String t      = rs.getString("title");
                    String org    = rs.getString("organization");
                    double rate   = rs.getDouble("rating");
                    String diff   = rs.getString("difficulty");
                    String enrolled = rs.getString("enrolled_students");

                    System.out.printf(
                      " %d. %s | %s | Rating: %.1f | Enrolled: %s | Difficulty: %s%n",
                      srn,t, org, rate, enrolled, diff
                    );
                    srn++;
                    System.out.println("-----------------------------------------");
                }
                if (!found) {
                    System.out.println("No courses match your interest.");
                }
            }

            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}

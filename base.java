import java.sql.*;

public class base {
    static final String DB_URL = "jdbc:mysql://localhost:3306/codesprint";
    static final String USER = "root";
    static final String PASS = "@Sanskruti22"; // replace with your actual password

    public static void main(String[] args) {
        try (
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement()
        ) {
            // First Query: Fetch top 10 courses
            String sql = "SELECT * FROM courses2 LIMIT 10";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("Top 10 courses:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String org = rs.getString("organization");
                double rating = rs.getDouble("rating");

                System.out.println(id + ": " + title + " | " + org + " | Rating: " + rating);
            }
            rs.close(); // Always close ResultSet after use
            String sql2 = "SELECT AVG(rating) AS avg_rating FROM courses2 WHERE organization = 'IBM'";
            System.out.println("\n=== Average Rating for IBM ===");
            try (ResultSet rs2 = stmt.executeQuery(sql2)) {
                if (rs2.next()) {
                    float avg = rs2.getFloat("avg_rating");
                    System.out.printf("Average rating for IBM: %.2f%n", avg);
                }
            }
            System.out.println("\nCourses with Difficulty = Intermediate");
            
            // Third Query: Fetch courses where difficulty is Beginner
            String sql3 = "SELECT * FROM courses2 WHERE difficulty = 'Intermediate'";
            try (ResultSet rs3 = stmt.executeQuery(sql3)) {
                System.out.println("\n=== Courses with Difficulty = Intermediate ===");
                while (rs3.next()) {
               int    id         = rs3.getInt("id");
                String title      = rs3.getString("title");
                String difficulty = rs3.getString("difficulty");
                System.out.printf("%d: %s | Difficulty: %s%n", id, title, difficulty);
    }
}


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

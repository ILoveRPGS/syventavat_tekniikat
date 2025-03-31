package tamk.ohsyte.commands;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import tamk.ohsyte.datamodel.Category;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeParseException;

@Command(name = "SQLAddevent")
public class SQLAddEventToDatabase implements Runnable {
    
    @Option(names = "--date", required = true, description = "Date of the event in the format yyyy-MM-dd or --MM-dd")
    String dateOptionString;

    @Option(names = "--category", required = true, description = "Category of the event")
    String categoryOptionString;

    @Option(names = "--description", required = true, description = "Description of the event")
    String descriptionOptionString;

    @Override
    public void run() {
        LocalDate eventDate;

        if (dateOptionString.startsWith("--")) {
            try {
                MonthDay monthDay = MonthDay.parse(dateOptionString);
                eventDate = LocalDate.of(9999, monthDay.getMonth(), monthDay.getDayOfMonth());
            } catch (DateTimeParseException e) {
                System.err.println("Invalid month-day format: '" + dateOptionString + "'. Expected '--MM-dd'.");
                return;
            }
        } else {
            try {
                eventDate = LocalDate.parse(dateOptionString);
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format: '" + dateOptionString + "'. Expected 'yyyy-MM-dd'.");
                return;
            }
        }

        // Parse category
        Category category;
        try {
            category = Category.parse(categoryOptionString);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid category: '" + categoryOptionString + "'.");
            return;
        }

        insertEventIntoDatabase(eventDate, descriptionOptionString, category);
    }

    private void insertEventIntoDatabase(LocalDate eventDate, String description, Category category) {
        String homeDirectory = System.getProperty("user.home");
        String configDirectory = ".today";
        Path databasePath = Paths.get(homeDirectory, configDirectory, "events.sqlite3");
        String url = "jdbc:sqlite:" + databasePath.toString();
        String sql = "INSERT INTO event (event_date, event_description, category_id) VALUES (?, ?, ?)";
        System.out.println(eventDate);
        System.out.println(description);
        System.out.println(category);

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, eventDate.toString());
            pstmt.setString(2, description);
            pstmt.setInt(3, getCategoryId(conn, category));
            
            pstmt.executeUpdate();
            System.out.println("Event added to the database.");
        } catch (SQLException e) {
            System.err.println("Error inserting event into database: " + e.getMessage());
        }
    }

    private int getCategoryId(Connection conn, Category category) throws SQLException {
        String query = "SELECT category_id FROM category WHERE primary_name = ? OR secondary_name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, category.toString());
            pstmt.setString(2, category.toString());
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("category_id");
            } else {
                throw new SQLException("Category not found in database. Please make sure it exists before adding events.");
            }
        }
    }
}
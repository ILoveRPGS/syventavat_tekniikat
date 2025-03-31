package tamk.ohsyte.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import tamk.ohsyte.datamodel.AnnualEvent;
import tamk.ohsyte.datamodel.Category;
import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.datamodel.SingularEvent;
import tamk.ohsyte.EventFactory;
import tamk.ohsyte.database.DatabaseManager;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeParseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Command(name = "addEvent", description = "Adds an event to the database.")
public class SQLAddEventToDatabase implements Runnable {

    @Option(names = "-d", required = true, description = "Date of the event (YYYY-MM-DD for singular, MM-dd for annual)")
    private String dateString;

    @Option(names = "-c", required = true, description = "Category of the event")
    private String categoryString;

    @Option(names = "-t", required = true, description = "Event title/description")
    private String title;

    @Override
    public void run() {
        try (Connection connection = DatabaseManager.getConnection()) {
            Category category = Category.parse(categoryString);
            Event event;
            String sql;

            if (dateString.length() == 10) { // Singular event
                LocalDate date = LocalDate.parse(dateString);
                event = new SingularEvent(title, category, date);
                sql = "INSERT INTO events (title, category, event_date, is_annual) VALUES (?, ?, ?, false)";
            } else if (dateString.length() == 5) { // Annual event
                MonthDay monthDay = MonthDay.parse("--" + dateString);
                event = new AnnualEvent(title, category, monthDay);
                sql = "INSERT INTO events (title, category, event_date, is_annual) VALUES (?, ?, ?, true)";
            } else {
                System.err.println("Invalid date format: " + dateString);
                return;
            }

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, event.getDescription());
                stmt.setString(2, event.getCategory().toString());
                stmt.setString(3, dateString);
                stmt.executeUpdate();
                System.out.println("Event added successfully: " + event.getDescription());
            }
        } catch (DateTimeParseException dtpe) {
            System.err.println("Invalid date format: " + dtpe.getMessage());
        } catch (SQLException sqle) {
            System.err.println("Database error: " + sqle.getMessage());
        } catch (IllegalArgumentException iae) {
            System.err.println("Invalid category: " + iae.getMessage());
        }
    }
}

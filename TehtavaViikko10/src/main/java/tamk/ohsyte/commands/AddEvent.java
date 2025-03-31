package tamk.ohsyte.commands;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeParseException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.datamodel.AnnualEvent;
import tamk.ohsyte.datamodel.SingularEvent;
import tamk.ohsyte.datamodel.Category;

@Command(name = "addevent")
public class AddEvent implements Runnable { 

    @Option(names = "--date", required = true, description = "Date of the event in the format yyyy-MM-dd")
    String dateOptionString;

    @Option(names = "--category", required = true, description = "Category of the event")
    String categoryOptionString;

    @Option(names = "--description", required = true, description = "Description of the event")
    String descriptionOptionString;

    @Option(names = "--provider", description = "Event provider identifier (optional)")
    String providerOptionString = "defaultProvider";  // Default value if not provided

    @Override
    public void run() {
       // Parse date
       LocalDate eventDate = null;
       MonthDay monthDay = null;
   
       if (dateOptionString.startsWith("--")) {
           // If the date starts with "--", it's in the MonthDay format ("--MM-dd")
           try {
               monthDay = MonthDay.parse(dateOptionString);  // Parse as MonthDay (e.g., --05-28)
           } catch (DateTimeParseException dtpe) {
               System.err.println("Invalid month-day format: '" + dateOptionString + "'. Expected '--MM-dd'.");
               return;
           }
       } else {
           // Otherwise, treat it as a full date (yyyy-MM-dd)
           try {
               eventDate = LocalDate.parse(dateOptionString);  // Parse as LocalDate (e.g., 2004-05-28)
           } catch (DateTimeParseException dtpe) {
               System.err.println("Invalid date string: '" + dateOptionString + "'. Expected 'yyyy-MM-dd'.");
               return;
           }
       }
   
       // Parse category
       Category category;
       try {
           category = Category.parse(categoryOptionString);
       } catch (IllegalArgumentException iae) {
           System.err.println("Invalid category string: '" + categoryOptionString + "'");
           return;
       }
   
       // Determine if event is annual or singular
       Event event;
       if (monthDay != null) {
           // If it's MonthDay (annual event)
           event = new AnnualEvent(monthDay, descriptionOptionString, category);
       } else {
           // If it's LocalDate (singular event)
           if (eventDate.getMonthValue() == LocalDate.now().getMonthValue() && eventDate.getDayOfMonth() == LocalDate.now().getDayOfMonth()) {
               event = new SingularEvent(eventDate, descriptionOptionString, category);
           } else {
               event = new AnnualEvent(MonthDay.of(eventDate.getMonth(), eventDate.getDayOfMonth()), descriptionOptionString, category);
           }
       }
   
       // Add the event to the CSV file
       addEventToCSV(event);
   }

    // Method to add the event to a CSV file (for CSVEventProvider)
    private void addEventToCSV(Event event) {
        String fileName = "events.csv";  // CSV file name

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Format the event as a CSV line
            String eventLine = String.format("%s,%s,%s", event.getMonthDay(), event.getCategory(), event.getDescription());
            writer.write(eventLine);
            writer.newLine();  // Add a newline after each event entry
            System.out.println("Event added to CSV: " + eventLine);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
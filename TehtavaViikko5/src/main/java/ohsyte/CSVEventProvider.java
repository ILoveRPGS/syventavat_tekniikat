package ohsyte;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.time.MonthDay;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
//import apache commons csv
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;


public class CSVEventProvider implements EventProvider {
    private List<Event> events;

    public CSVEventProvider(String fileName) {
        this.events = new ArrayList<>();
        
        try {
            FileReader reader = new FileReader(Paths.get(fileName).toFile());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

            for (CSVRecord record : records) {
                this.events.add(makeEvent(record));
            }

            System.out.printf("Read %d events from CSV file%n", this.events.size());
        } catch (IOException ioe) {
            System.err.println("File '" + fileName + "' not found");
        }
    }

    @Override
    public List<Event> getEvents() {
        return this.events;
    }

    @Override
    public List<Event> getEventsOfCategory(Category category) {
        List<Event> result = new ArrayList<Event>();
        for (Event event : this.events) {
            if (event.getCategory().equals(category)) {
                result.add(event);
            }
        }
        return result;
    }

    @Override
    public List<Event> getEventsOfDate(MonthDay monthDay) {
        List<Event> result = new ArrayList<Event>();

        for (Event event : this.events) {
            final Month eventMonth = event.getDate().getMonth();
            final int eventDay = event.getDate().getDayOfMonth();
            if (monthDay.getMonth() == eventMonth && monthDay.getDayOfMonth() == eventDay) {
                result.add(event);
            }
        }

        return result;
    }

    private Event makeEvent(CSVRecord row) {
        LocalDate date = LocalDate.parse(row.get(0));
        String description = row.get(1);
        String categoryString = row.get(2);
        String[] categoryParts = categoryString.split("/");
        String primary = categoryParts[0];
        String secondary = null;
        if (categoryParts.length == 2) {
            secondary = categoryParts[1];
        }
        Category category = new Category(primary, secondary);
        return new Event(date, description, category);
    }
}
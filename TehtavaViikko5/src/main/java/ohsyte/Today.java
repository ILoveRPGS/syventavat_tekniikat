package ohsyte;

import java.util.List;
import java.nio.file.Paths;

public class Today {
    public static void main(String[] args) {
        // Selvitetään käyttöjärjestelmän perusteella kotihakemisto
        String homeDirectory = System.getenv("USERPROFILE"); // Windows
        if (homeDirectory == null) {
            homeDirectory = System.getenv("HOME"); // Mac/Linux
        }

        // Varmistetaan, että kotihakemisto löytyi
        if (homeDirectory != null) {
            String fileName = Paths.get(homeDirectory, ".today", "events.csv").toString();
            System.out.println("CSV-tiedoston polku: " + fileName);

            // Käytä tätä fileNamea CSVEventProviderin luomiseen
            EventProvider provider = new CSVEventProvider(fileName);

            // Esimerkki, miten voit käyttää provideria
            List<Event> events = provider.getEvents();
            System.out.println("Kaikki tapahtumat:");
            for (Event event : events) {
                System.out.println(event);
            }
        } else {
            System.err.println("Kotihakemistoa ei löytynyt.");
        }
    }
}
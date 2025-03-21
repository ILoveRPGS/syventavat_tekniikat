package tamk.ohsyte;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import tamk.ohsyte.datamodel.Event;

import tamk.ohsyte.commands.ListProviders;
import tamk.ohsyte.commands.ListEvents;
import tamk.ohsyte.datamodel.*;
import tamk.ohsyte.providers.CSVEventProvider;
import tamk.ohsyte.providers.web.EventDeserializer;
import tamk.ohsyte.providers.web.WebEventProvider;

@Command(name = "today", subcommands = { ListProviders.class, ListEvents.class }, description = "Shows events from history and annual observations")
public class Today {
    public Today() {
        // Get the singleton manager. Later calls to getInstance
        EventManager manager = EventManager.getInstance();

        String homeDirectory = System.getProperty("user.home");
        String configDirectory = ".today";
        Path path = Paths.get(homeDirectory, configDirectory, "events.csv");

        // Create the events file if it doesn't exist
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                System.err.println("Unable to create events file");
                System.exit(1);
            }
        }

        String eventProviderId = "standard";


        MonthDay monthDay = MonthDay.now();
        //testataan haluttua päivää
        //MonthDay
        //monthDay = MonthDay.of(3, 24);
        String serverAddress = "https://todayserver-89bb2a1b2e80.herokuapp.com/";
        String serverEventsPath = "api/v1/events";
        String eventsParameters = String.format("?date=%s", monthDay.toString().substring(2));

        String bodyString = null;
        System.out.println("Fetching events from the server...");

        try {
            String serverUriString = String.format("%s%s%s", serverAddress, serverEventsPath, eventsParameters);
            URI serverUri = new URI(serverUriString);

            //Annetaan serverUri ja eventProviderId constructorille
            WebEventProvider webEventProvider = new WebEventProvider(serverUri, eventProviderId);

            //Haetaan tapahtumat
            List<Event> eventsOfToday = webEventProvider.getEventsOfDate(monthDay.toString()); 
            //Filter events with Singular and Annual events
            List<AnnualEvent> annualEvents = new ArrayList<>();
            List<SingularEvent> singularEvents = new ArrayList<>();
            for (Event event : eventsOfToday) {
                if (event instanceof AnnualEvent) {
                    annualEvents.add((AnnualEvent) event);
                } else if (event instanceof SingularEvent) {
                    singularEvents.add((SingularEvent) event);
                }
            }
            // Just print the events (just like commands.ListEvents)
            if (!annualEvents.isEmpty()) {
                System.out.println("Observed today:");
                Collections.sort(annualEvents, new AnnualEventComparator());

                for (AnnualEvent a : annualEvents) {
                    System.out.printf(
                            "- %s (%s) %n",
                            a.getDescription(),
                            a.getCategory());
                }
            }

            if (!singularEvents.isEmpty()) {
                System.out.println("---web".repeat(10));
                System.out.println("Today in history (events from web):");
                Collections.sort(singularEvents, new SingularEventComparator());
                Collections.reverse(singularEvents);

                for (SingularEvent s : singularEvents) {
                    int year = s.getDate().getYear();

                    System.out.printf(
                            "%d: %s (%s)%n",
                            year,
                            s.getDescription(),
                            s.getCategory());
                }

                System.out.println("---web".repeat(10));
            }
        } catch (JsonProcessingException ex) {
            System.err.println("Error processing JSON: " + ex.toString());
            ex.printStackTrace();

        } catch (URISyntaxException | InterruptedException | IOException ex) {
            System.err.println("Error processing request: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
            int exitCode = new CommandLine(new Today()).execute(args);
            System.exit(exitCode);
        }
    }

package main.java.tamk.ohsyte;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class Today {
    public static void main(String[] args) {
        // Gets the singleton manager. Later calls to getInstance
        // will return the same reference.
        EventManager manager = EventManager.getInstance();

        // Add a CSV event provider that reads from the given file.
        // Replace with a valid path to the events.csv file on your own computer!
        String homeDirectory = System.getenv("USERPROFILE"); // Windows
        if (homeDirectory == null) {
            homeDirectory = System.getenv("HOME"); // Mac/Linux
        }
        final String fileName = Paths.get(homeDirectory, ".today", "events.csv").toString();
        manager.addEventProvider(new CSVEventProvider(fileName));

        final String fileName2 = Paths.get(homeDirectory, ".today", "singular-events.csv").toString();
        manager.addEventProvider(new CSVEventProvider(fileName2));

        MonthDay today = MonthDay.now();
        List<Event> allEvents = manager.getAllEvents();
        /*List<AnnualEvent> annualEvents = new ArrayList<>();
        List<SingularEvent> singularEvents = new ArrayList<>();
        for (Event event : allEvents) {
            if (event instanceof AnnualEvent) {
                annualEvents.add((AnnualEvent) event);
            } else if (event instanceof SingularEvent) {
                singularEvents.add((SingularEvent) event);
            }
        }

        System.out.println("Today:");
        Collections.sort(annualEvents, new AnnualEventComparator());

        for (AnnualEvent a : annualEvents) {
            System.out.printf(
                    "- %s (%s) %n",
                    a.getDescription(),
                    a.getCategory());
        }
        //System.out.printf("%d events%n", annualEvents.size());

        System.out.println("\nToday in history:");
        Collections.sort(singularEvents, new SingularEventComparator());
        Collections.reverse(singularEvents);

        for (SingularEvent s : singularEvents) {
            int year = s.getDate().getYear();
            if (year < 2015) {
                continue;
            }

            System.out.printf(
                    "%d: %s (%s)%n",
                    year,
                    s.getDescription(),
                    s.getCategory());
        }*/
        //System.out.printf("%d events%n", singularEvents.size());
         //Use the mehtod getFilteredEvents to get the events that match the filter
    Category category = new Category("test", "test");
    List<Event> events = EventFilter.getFilteredEvents(new EventFilter.CategoryFilter(category));
    System.out.println("Filtered events by CategoryFilter: ");

    List<AnnualEvent> annualEventsCategory = new ArrayList<>();
    List<SingularEvent> singularEventsCategory = new ArrayList<>();
    for (Event event : events) {
        if (event instanceof AnnualEvent) {
            annualEventsCategory.add((AnnualEvent) event);
        } else if (event instanceof SingularEvent) {
            singularEventsCategory.add((SingularEvent) event);
        }
    }

    System.out.println("Annuals by Category:");
    Collections.sort(annualEventsCategory, new AnnualEventComparator());

    for (AnnualEvent a : annualEventsCategory) {
        System.out.printf(
                "- %s (%s) %n",
                a.getDescription(),
                a.getCategory());
    }
    //System.out.printf("%d events%n", annualEvents.size());

    System.out.println("\nSingulars by Category:");
    Collections.sort(singularEventsCategory, new SingularEventComparator());
    Collections.reverse(singularEventsCategory);

    for (SingularEvent s : singularEventsCategory) {
        int year = s.getDate().getYear();
        if (year < 2015) {
            continue;
        }

        System.out.printf(
                "%d: %s (%s)%n",
                year,
                s.getDescription(),
                s.getCategory());
    }


    List<Event> events2 = EventFilter.getFilteredEvents(new EventFilter.DateFilter(today));
    System.out.println("\nFiltered events by DateFilter: ");

    List<AnnualEvent> annualEventsDate = new ArrayList<>();
    List<SingularEvent> singularEventsDate = new ArrayList<>();
    for (Event event : events2) {
        if (event instanceof AnnualEvent) {
            annualEventsDate.add((AnnualEvent) event);
        } else if (event instanceof SingularEvent) {
            singularEventsDate.add((SingularEvent) event);
        }
    }

    System.out.println("Annuals by this Date:");
    Collections.sort(annualEventsDate, new AnnualEventComparator());

    for (AnnualEvent a : annualEventsDate) {
        System.out.printf(
                "- %s (%s) %n",
                a.getDescription(),
                a.getCategory());
    }
    //System.out.printf("%d events%n", annualEvents.size());

    System.out.println("\nSingulars by this Date:");
    Collections.sort(singularEventsDate, new SingularEventComparator());
    Collections.reverse(singularEventsDate);

    for (SingularEvent s : singularEventsDate) {
        int year = s.getDate().getYear();
        if (year < 2015) {
            continue;
        }

        System.out.printf(
                "%d: %s (%s)%n",
                year,
                s.getDescription(),
                s.getCategory());
    }
    List<Event> events3 = EventFilter.getFilteredEvents(new EventFilter.DateCategoryFilter(today, category));
    System.out.println("\nFiltered events by DateCategoryFilter: ");

    List<AnnualEvent> annualEventsDateCategory = new ArrayList<>();
    List<SingularEvent> singularEventsDateCategory = new ArrayList<>();
    for (Event event : events3) {
        if (event instanceof AnnualEvent) {
            annualEventsDateCategory.add((AnnualEvent) event);
        } else if (event instanceof SingularEvent) {
            singularEventsDateCategory.add((SingularEvent) event);
        }
    }

    System.out.println("Annuals by this Date and Category:");
    Collections.sort(annualEventsDate, new AnnualEventComparator());

    for (AnnualEvent a : annualEventsDateCategory) {
        System.out.printf(
                "- %s (%s) %n",
                a.getDescription(),
                a.getCategory());
    }
    //System.out.printf("%d events%n", annualEvents.size());

    System.out.println("\nSingulars by this Date and Category:");
    Collections.sort(singularEventsDateCategory, new SingularEventComparator());
    Collections.reverse(singularEventsDateCategory);

    for (SingularEvent s : singularEventsDateCategory) {
        int year = s.getDate().getYear();
        if (year < 2015) {
            continue;
        }

        System.out.printf(
                "%d: %s (%s)%n",
                year,
                s.getDescription(),
                s.getCategory());
    }
}
}

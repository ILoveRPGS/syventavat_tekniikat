package main.java.tamk.ohsyte;

import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;

public abstract class EventFilter {
    // Filters based on category, description, or month-day
    private MonthDay monthDay;
    private String description;
    private Category category;

    public EventFilter(MonthDay monthDay, String description, Category category) {
        this.monthDay = monthDay;
        this.description = description;
        this.category = category;
    }

    public MonthDay getMonthDay() {
        return monthDay;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    // Abstract method that subclasses need to implement
    public abstract boolean accepts(Event event);

    // Filter by date
    public static class DateFilter extends EventFilter {
        private MonthDay criteriaDate;
        private Integer criteriaYear;
    
        // Suodatin vain päivämäärän perusteella
        public DateFilter(MonthDay criteriaDate) {
            super(criteriaDate, "", null);
            this.criteriaDate = criteriaDate;
        }
    
        // Suodatin myös vuoden perusteella
        public DateFilter(MonthDay criteriaDate, Integer criteriaYear) {
            super(criteriaDate, "", null);
            this.criteriaDate = criteriaDate;
            this.criteriaYear = criteriaYear;
        }
    
        @Override
        public boolean accepts(Event event) {
            // Jos vuoden suodatusta ei ole, verrataan vain kuukauden ja päivän osalta
            if (criteriaYear == null) {
                return event.getMonthDay().equals(criteriaDate);
            }
    
            // Jos tapahtuma on SingularEvent (sisältää vuoden)
            if (event instanceof SingularEvent) {
                SingularEvent singularEvent = (SingularEvent) event;
                return singularEvent.getMonthDay().equals(criteriaDate) && singularEvent.getYear() == criteriaYear;
            }
    
            // Jos tapahtuma on AnnualEvent (ei sisällä vuotta)
            if (event instanceof AnnualEvent) {
                AnnualEvent annualEvent = (AnnualEvent) event;
                return annualEvent.getMonthDay().equals(criteriaDate);
            }
    
            // Jos tapahtuma ei ole SingularEvent eikä AnnualEvent, ei hyväksytä
            return false;
        }
    }

    // Filter by category
    public static class CategoryFilter extends EventFilter {
        private Category criteriaCategory;
    
        public CategoryFilter(Category criteriaCategory) {
            super(null, "", criteriaCategory);  // This might not be needed depending on your implementation of EventFilter
            this.criteriaCategory = criteriaCategory;
        }
    
        @Override
        public boolean accepts(Event event) {
            // Get the event's category
            Category eventCategory = event.getCategory();
            
            // Compare the primary and secondary categories
            boolean isPrimaryMatch = eventCategory.getPrimary().equals(criteriaCategory.getPrimary());
            boolean isSecondaryMatch = (eventCategory.getSecondary() != null && eventCategory.getSecondary().equals(criteriaCategory.getSecondary()));
            
            return isPrimaryMatch || isSecondaryMatch;  // Return true if either primary or secondary category matches
        }
    }
    

    // Filter by both date and category
    public static class DateCategoryFilter extends EventFilter {
        private MonthDay criteriaDate;
        private Category criteriaCategory;
    
        public DateCategoryFilter(MonthDay criteriaDate, Category criteriaCategory) {
            super(criteriaDate, "", criteriaCategory);
            this.criteriaDate = criteriaDate;
            this.criteriaCategory = criteriaCategory;
        }
    
        @Override
        public boolean accepts(Event event) {
            // Check if the event's date matches the criteria date
            boolean isDateMatch = event.getMonthDay().equals(criteriaDate);
    
            // Compare primary and secondary categories
            boolean isPrimaryMatch = event.getCategory().getPrimary().equals(criteriaCategory.getPrimary());
            boolean isSecondaryMatch = (event.getCategory().getSecondary() != null && event.getCategory().getSecondary().equals(criteriaCategory.getSecondary()));
            
            // Return true if both date and category (primary or secondary) match
            return isDateMatch && (isPrimaryMatch || isSecondaryMatch);
        }
    }
    

    // Method to filter events based on any filter criteria
    public static List<Event> getFilteredEvents(EventFilter filter) {
        List<Event> events = EventManager.getInstance().getAllEvents();
        List<Event> filteredEvents = new ArrayList<>();
        
        for (Event event : events) {
            if (filter.accepts(event)) {
                filteredEvents.add(event);
            }
        }
        
        return filteredEvents;
    }
}

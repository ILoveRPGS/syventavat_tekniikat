package tamk.ohsyte;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;


import tamk.ohsyte.datamodel.AnnualEvent;
import tamk.ohsyte.datamodel.AnnualEventComparator;
import tamk.ohsyte.datamodel.Category;
import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.datamodel.SingularEvent;
import tamk.ohsyte.datamodel.SingularEventComparator;
import tamk.ohsyte.datamodel.filters.*;
import tamk.ohsyte.providers.EventProvider;


/**
 * Manages and queries the events available from event providers.
 */
public class EventManager {
    private static final EventManager INSTANCE = new EventManager();

    private final List<EventProvider> eventProviders;

    private EventManager() {
        this.eventProviders = new ArrayList<>();
    }

    /**
     * Gets the only instance of the event manager.
     *
     * @return the instance
     */
    public static EventManager getInstance() {
        return INSTANCE;
    }

    /**
     * Adds an event provider to the manager's list if it isn't
     * already there.

     * @param provider the event provider to add
     * @return <code>true</code> if the provider was added, <code>false</code> otherwise
     */
    public boolean addEventProvider(EventProvider provider) {
        // TODO: Check if this event provider is already on the list.
        this.eventProviders.add(provider);
        return true;
    }


    /**
     * Removes the specified event provider from the manager's list.
     *
     * @param providerId the identifier of the event provider to remove
     * @return <code>true</code> if the provider was removed, <code>false</code> if not
     */
    public boolean removeEventProvider(String providerId) {
        throw new UnsupportedOperationException(
            "Removal of event providers is not supported yet.");
    }

    /**
     * Get all the events available from all registered event providers.
     *
     * @return the list of all events
     */
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();

        for (EventProvider provider : this.eventProviders) {
            events.addAll(provider.getEvents());
        }

        return events;
    }

    public List<Event> getEventsOfDate(MonthDay monthDay) {
        List<Event> events = new ArrayList<>();

        for (EventProvider provider : this.eventProviders) {
            events.addAll(provider.getEventsOfDate(monthDay));
        }

        return events;
    }

        /**
         * Gets the number of event providers for the manager.
         *
         * @return the number of event providers
         */
    public int getEventProviderCount() {
        return this.eventProviders.size();
    }

    /**
     * Gets the identifiers of all event providers of the manager.
     *
     * @return list of provider identifiers
     */
    public List<String> getEventProviderIdentifiers() {
        List<String> ids = new ArrayList<>();

        for (EventProvider provider : this.eventProviders) {
            ids.add(provider.getIdentifier());
        }

        return ids;
    }

    /**
     * Gets the events that are accepted by the specified filter.
     *
     * @param filter the filter
     * @return list of events
     */
    public List<Event> getFilteredEvents(EventFilter filter) {
        List<Event> result = new ArrayList<>();

        for (Event event : this.getAllEvents()) {
            if (filter.accepts(event)) {
                result.add(event);
            }
        }

        return result;
    }
}

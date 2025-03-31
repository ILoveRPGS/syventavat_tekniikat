package tamk.ohsyte.providers.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import tamk.ohsyte.datamodel.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class WebEventProvider {
    private URI serverUri;
    private String eventProviderId;

    // Konstruktori, joka ottaa vastaan palvelimen URI:n ja tapahtumien tarjoajan ID:n
    public WebEventProvider(URI serverUri, String eventProviderId) {
        this.serverUri = serverUri;
        this.eventProviderId = eventProviderId;
    }

    // Metodi hakee tapahtumat annetulta päivämäärältä ja palauttaa ne listana
    public List<Event> getEventsOfDate(String date) throws InterruptedException, IOException {
        String bodyString = null;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(serverUri)
                .GET()
                .build();
        
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            bodyString = response.body();
        } catch (InterruptedException | IOException e) {
            throw e; // Uudelleenheitetään poikkeus kutsujalle
        }

        // Deserializoidaan JSON-muotoinen data Event-olioiksi
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule("EventDeserializer");
            module.addDeserializer(Event.class, new EventDeserializer());
            mapper.registerModule(module);

            // Parsetaan JSON-muotoinen data Event-olioiksi
            List<Event> allEvents = mapper.readValue(bodyString, mapper.getTypeFactory().constructCollectionType(List.class, Event.class));
            
            // Jaetaan tapahtumat vuosittaisiin ja yksittäisiin tapahtumiin
            List<AnnualEvent> annualEvents = new ArrayList<>();
            List<SingularEvent> singularEvents = new ArrayList<>();
            
            for (Event event : allEvents) {
                if (event instanceof AnnualEvent) {
                    annualEvents.add((AnnualEvent) event);
                } else if (event instanceof SingularEvent) {
                    singularEvents.add((SingularEvent) event);
                }
            }
            
            // Luodaan uusi lista, johon lisätään vuosittaiset ja yksittäiset tapahtumat
            List<Event> filteredEvents = new ArrayList<>();
            filteredEvents.addAll(annualEvents);
            filteredEvents.addAll(singularEvents); 

            return filteredEvents;

        } catch (JsonProcessingException e) {
            System.err.println("Error processing JSON: " + e.getMessage());
            throw e; // Uudelleenheitetään poikkeus kutsujalle
        }
    }
}

package com.calendarapi.lbprojektm165calendarapi.SmokeTest;

import com.calendarapi.lbprojektm165calendarapi.controller.EventController;
import com.calendarapi.lbprojektm165calendarapi.service.EventService;
import com.calendarapi.lbprojektm165calendarapi.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SmokeT {
      
    // Injected Komponenten (werden automatisch vom Spring Context geladen)
    @Autowired
    private EventController eventController;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MockMvc mockMvc;

    // 1. Testet, ob der ApplicationContext erfolgreich geladen wird
    @Test
    void contextLoads() {
        // Kein Inhalt nötig – schlägt fehl, wenn App nicht startet
    }

    // 2. Prüft, ob der EventController korrekt geladen wurde
    @Test
    void eventControllerIsLoaded() {
        assertThat(eventController).isNotNull();
    }

    // 3. Prüft, ob der EventService korrekt geladen wurde
    @Test
    void eventServiceIsLoaded() {
        assertThat(eventService).isNotNull();
    }

    // 4. Prüft, ob MongoDB erreichbar ist und die Collection "events" existiert
    @Test
    void mongoDbIsAvailable() {
        boolean exists = mongoTemplate.collectionExists("events");
        assertThat(exists).isTrue();
    }

    // 5. Testet, ob der GET-Endpunkt "/api/events" erfolgreich erreichbar ist
    @Test
    void eventEndpointIsReachable() throws Exception {
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk()); // 200 OK wird erwartet
    }

    // 6. Prüft, ob das EventRepository vom Spring Context geladen wurde
    @Test
    void eventRepositoryIsLoaded() {
        assertThat(eventRepository).isNotNull();
    }

    // 7. Führt eine einfache Abfrage gegen das Repository aus – erwartet kein Fehler
    @Test
    void mongoDbQueryWorks() {
        assertThat(eventRepository.findAll()).isNotNull();
    }

    // 8. Sendet einen ungültigen POST-Request an "/api/events" – erwartet 400 Bad Request
    @Test
    void createEventWithInvalidDataReturns400() throws Exception {
        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}") // Leerer JSON-Body → ungültig
        ).andExpect(status().isBadRequest());
    }
}

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Smoke-Test-Klasse zur Überprüfung, ob alle Hauptkomponenten der Anwendung korrekt geladen werden
 * und grundlegende Endpunkte verfügbar sind.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CalendarAPISmokeTest {

    @Autowired
    private EventController eventController;

    @Autowired
    private EventService eventService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    /**
     * Prüft, ob der ApplicationContext korrekt geladen wird.
     * Wenn der Test fehlschlägt, startet die App nicht korrekt.
     */
    @Test
    void contextLoads() {
        // Prüft, ob der ApplicationContext ohne Fehler lädt
    }

    /**
     * Überprüft, ob der EventController korrekt vom Spring Context geladen wurde.
     */
    @Test
    void eventControllerIsLoaded() {
        assertThat(eventController).isNotNull();
    }

    /**
     * Überprüft, ob der EventService korrekt geladen wurde.
     */
    @Test
    void eventServiceIsLoaded() {
        assertThat(eventService).isNotNull();
    }

    /**
     * Überprüft, ob die MongoDB-Verbindung funktioniert und die Collection "events" existiert.
     */
    @Test
    void mongoDbIsAvailable() {
        boolean exists = mongoTemplate.collectionExists("events");
        assertThat(exists).isTrue();
    }

    /**
     * Führt einen GET-Request auf den Events-Endpunkt aus, um sicherzustellen,
     * dass dieser erreichbar ist und erfolgreich antwortet.
     */
    @Test
    void eventEndpointIsReachable() throws Exception {
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk());
    }

    /**
     * Überprüft, ob das EventRepository korrekt geladen wurde.
     */
    @Test
    void eventRepositoryIsLoaded() {
        assertThat(eventRepository).isNotNull();
    }

    /**
     * Führt eine einfache Abfrage auf dem Repository aus, um sicherzustellen,
     * dass die Datenbankabfrage funktioniert.
     */
    @Test
    void mongoDbQueryWorks() {
        assertThat(eventRepository.findAll()).isNotNull();
    }

    /**
     * Simuliert einen POST-Request mit ungültigen (leeren) Daten,
     * um sicherzustellen, dass die API eine 400 Bad Request zurückgibt.
     */
    @Test
    void createEventWithInvalidDataReturns400() throws Exception {
        mockMvc.perform(
                post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}") // Leerer Body → sollte 400 auslösen
        ).andExpect(status().isBadRequest());
    }
}

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
 * und grundlegende REST-Endpunkte erreichbar und funktional sind.
 *
 * <p>Diese Tests helfen dabei sicherzustellen, dass die grundlegende Infrastruktur der Anwendung intakt ist:
 * Spring-Kontext, MongoDB-Verbindung, Controller, Services, Repositories und grundlegende HTTP-Endpunkte.</p>
 *
 * @author Ricardo
 */
@SpringBootTest
@AutoConfigureMockMvc
class SmokeT { // Optional: umbenennen in SmokeTest

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
     * Testet, ob der Spring ApplicationContext korrekt geladen wird.
     *
     * <p>Dies ist ein grundlegender Test: Wenn er fehlschlägt, funktioniert die gesamte App nicht.</p>
     */
    @Test
    void contextLoads() {
        // Erfolgreicher Durchlauf = Context wurde geladen
    }

    /**
     * Testet, ob der {@link EventController} korrekt vom Spring-Container instanziiert wurde.
     */
    @Test
    void eventControllerIsLoaded() {
        assertThat(eventController).isNotNull();
    }

    /**
     * Testet, ob der {@link EventService} korrekt geladen wurde.
     */
    @Test
    void eventServiceIsLoaded() {
        assertThat(eventService).isNotNull();
    }

    /**
     * Überprüft, ob die Verbindung zur MongoDB funktioniert und die Collection {@code events} existiert.
     */
    @Test
    void mongoDbIsAvailable() {
        boolean exists = mongoTemplate.collectionExists("events");
        assertThat(exists).isTrue();
    }

    /**
     * Testet, ob der GET-Endpunkt {@code /api/events} erreichbar ist und einen HTTP 200 OK zurückliefert.
     *
     * @throws Exception falls die Anfrage fehlschlägt
     */
    @Test
    void eventEndpointIsReachable() throws Exception {
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk());
    }

    /**
     * Testet, ob das {@link EventRepository} erfolgreich im Spring-Kontext vorhanden ist.
     */
    @Test
    void eventRepositoryIsLoaded() {
        assertThat(eventRepository).isNotNull();
    }

    /**
     * Führt eine einfache Datenbankabfrage mit {@link EventRepository#findAll()} durch,
     * um sicherzustellen, dass der Zugriff auf MongoDB funktioniert.
     */
    @Test
    void mongoDbQueryWorks() {
        assertThat(eventRepository.findAll()).isNotNull();
    }

    /**
     * Führt einen POST-Request mit einem ungültigen (leeren) JSON-Body aus
     * und erwartet eine HTTP 400 Bad Request-Antwort von der API.
     *
     * @throws Exception falls die Anfrage fehlschlägt
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

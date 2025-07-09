package com.calendarapi.lbprojektm165calendarapi.controller;

import com.calendarapi.lbprojektm165calendarapi.model.Event;
import com.calendarapi.lbprojektm165calendarapi.service.EventService;
import com.calendarapi.lbprojektm165calendarapi.dto.FilterDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Simuliert HTTP-Anfragen an den Controller

    @MockitoBean
    private EventService eventService;  // Mock des Services, keine echte DB-Verbindung nötig

    private static final String BASE = "/api/events";  // Basis-URL aller Endpunkte

    @Test
    @DisplayName("GET /api/events/hello → Hello World!")
    void testHello() throws Exception {
        // GET-Request auf /hello und Überprüfung, dass "Hello World!" zurückkommt
        mockMvc.perform(get(BASE + "/hello"))
                .andExpect(status().isOk())              // Status 200
                .andExpect(content().string("Hello World!"));  // Body stimmt überein
    }

    @Test
    @DisplayName("POST /api/events mit gültigem Cron → 200 + zurückgegebenes Event")
    void testCreateEvent_ValidCron() throws Exception {
        // --- 1. Vorbereitung der Eingabedaten ---
        // Neues Event-Objekt, so wie es der Controller empfängt
        Event input = new Event();
        input.setCron("0 0 * * * ?");      // gültiges Cron-Pattern
        input.setTitle("Test-Title");      // beliebiger Titel

        // --- 2. Simuliertes Verhalten des Service ---
        // Wir erstellen ein "saved"-Objekt, das der Service zurückgeben soll
        Event saved = new Event();
        saved.setId(UUID.randomUUID().toString());    // Datenbank würde eine ID generieren
        saved.setCron(input.getCron());               // gleiche Cron-Daten
        saved.setTitle(input.getTitle());             // gleicher Titel
        // Mockito: Wenn createEvent aufgerufen wird, liefern wir unser "saved" zurück
        when(eventService.createEvent(any(Event.class))).thenReturn(saved);

        // --- 3. Der JSON-Request-Body --- Hier stellen wir dar, wie die HTTP-Anfrage aussieht
        String jsonBody = """
            {                   
              "cron":"0 0 * * * ?",
              "title":"Test-Title"
            }
            """;

        // --- 4. Ausführung der Anfrage und Überprüfung der Antwort ---
        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andDo(print())  // Dies zeigt Details der Anfrage/Antwort
                .andExpect(status().isOk());


        // --- 5. Argumentprüfung: Wurde das korrekte Objekt an den Service übergeben? ---
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(eventService).createEvent(captor.capture());  // fängt das übergebene Event ab
        // Prüfen, dass es genau das erwartete Cron-Pattern enthält
        assertThat(captor.getValue().getCron()).isEqualTo("0 0 * * * ?");
    }

    @Test
    @DisplayName("POST /api/events mit ungültigem Cron → 400 Bad Request")
    void testCreateEvent_InvalidCron() throws Exception {
        String badCron = "invalid-cron";
        // JSON mit ungültigem Cron-Pattern
        String jsonBody = String.format("""
            {
              "cron":"%s",
              "title":"X"
            }
            """, badCron);

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())        // HTTP 400 erwartet
                .andExpect(status().reason("Ungültiges Cron-Pattern: " + badCron));

        // Der Service wird nie aufgerufen, weil Validierung vorher scheitert
        verifyNoInteractions(eventService);
    }

    @Test
    @DisplayName("GET /api/events/{id} → 200 + Event")
    void testGetById() throws Exception {
        String id = UUID.randomUUID().toString();
        Event found = new Event();
        found.setId(id);
        found.setCron("0 0 * * * ?");
        found.setTitle("FetchTest");
        when(eventService.getById(id)).thenReturn(found);

        // GET mit Pfadparameter und Überprüfung, dass ID und Titel zurückgegeben werden
        mockMvc.perform(get(BASE + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("FetchTest"));
    }

    @Test
    @DisplayName("DELETE /api/events/{id} → 204 No Content")
    void testDelete() throws Exception {
        String id = UUID.randomUUID().toString();
        doNothing().when(eventService).deleteEvent(id);

        // DELETE-Anfrage und Prüfung auf 204 No Content
        mockMvc.perform(delete(BASE + "/" + id))
                .andExpect(status().isNoContent());

        // Service-Methode wurde aufgerufen
        verify(eventService).deleteEvent(id);
    }

    @Test
    @DisplayName("GET /api/events mit Filterparametern → 200 + Liste")
    void testListEvents_WithFilter() throws Exception {
        // Mock-Service liefert leere Liste
        when(eventService.listEvents(any(FilterDto.class)))
                .thenReturn(Collections.emptyList());

        // GET mit Query-Parametern weekday und from
        mockMvc.perform(get(BASE)
                        .param("weekday", "MONDAY")
                        .param("from", "2025-07-01T00:00:00Z"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));  // leere JSON-Liste

        // Prüfen, ob Filter-DTO korrekt befüllt wurde
        ArgumentCaptor<FilterDto> captor = ArgumentCaptor.forClass(FilterDto.class);
        verify(eventService).listEvents(captor.capture());
        assertThat(captor.getValue().getWeekday()).isEqualTo("MONDAY");
        assertThat(captor.getValue().getFrom()).isEqualTo("2025-07-01T00:00:00Z");
    }
}

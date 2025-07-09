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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Ersetzt @MockBean (deprecated) durch @MockitoBean
     * Mockt den EventService, damit keine echte Datenbankverbindung nötig ist.
     */
    @MockitoBean
    private EventService eventService;

    private static final String BASE = "/api/events";

    @Test
    @DisplayName("GET /api/events/hello → Hello World!")
    void testHello() throws Exception {
        // Führt einen GET-Request auf /api/events/hello aus und erwartet "Hello World!"
        mockMvc.perform(get(BASE + "/hello"))
                .andExpect(status().isOk())                // HTTP 200
                .andExpect(content().string("Hello World!")); // Antwort-Body exakt
    }

    @Test
    @DisplayName("POST /api/events mit gültigem Cron → 200 + zurückgegebenes Event")
    void testCreateEvent_ValidCron() throws Exception {
        // 1) Eingabe-Event mit gültigem Cron-Pattern vorbereiten
        Event in = new Event();
        in.setCron("0 0 * * * ?");
        in.setTitle("Test-Title");

        // 2) Service-Mock so konfigurieren, dass es ein Event mit ID zurückgibt
        Event out = new Event();
        out.setId(UUID.randomUUID().toString());
        out.setCron(in.getCron());
        out.setTitle(in.getTitle());
        when(eventService.createEvent(any(Event.class))).thenReturn(out);

        // JSON-Payload für den POST-Request
        String json = """
            {
              "cron":"0 0 * * * ?",
              "title":"Test-Title"
            }
            """;

        // Request ausführen und Antwort prüfen
        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())                // HTTP 200
                .andExpect(jsonPath("$.id").value(out.getId()))
                .andExpect(jsonPath("$.cron").value(in.getCron()))
                .andExpect(jsonPath("$.title").value(in.getTitle()));

        // Mit ArgumentCaptor verifizieren, dass der Controller das richtige Event ans Service weiterreicht
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(eventService).createEvent(captor.capture());
        assertThat(captor.getValue().getCron()).isEqualTo("0 0 * * * ?");
    }

    @Test
    @DisplayName("POST /api/events mit ungültigem Cron → 400 Bad Request")
    void testCreateEvent_InvalidCron() throws Exception {
        String badCron = "invalid-cron";
        // JSON-Payload mit ungültigem Cron
        String json = String.format("""
            {
              "cron":"%s",
              "title":"X"
            }
            """, badCron);

        // Request ausführen und nur Status + Reason prüfen (kein JSON im Body)
        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())                 // HTTP 400
                .andExpect(status().reason("Ungültiges Cron-Pattern: " + badCron));

        // Sicherstellen, dass das Service gar nicht aufgerufen wurde
        verifyNoInteractions(eventService);
    }

    @Test
    @DisplayName("GET /api/events/{id} → 200 + Event")
    void testGetById() throws Exception {
        String id = UUID.randomUUID().toString();
        // Service-Mock liefert ein bestimmtes Event zurück
        Event e = new Event();
        e.setId(id);
        e.setCron("0 0 * * * ?");
        e.setTitle("FetchTest");
        when(eventService.getById(id)).thenReturn(e);

        // Request ausführen und JSON-Felder prüfen
        mockMvc.perform(get(BASE + "/" + id))
                .andExpect(status().isOk())                // HTTP 200
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("FetchTest"));
    }

    @Test
    @DisplayName("DELETE /api/events/{id} → 204 No Content")
    void testDelete() throws Exception {
        String id = UUID.randomUUID().toString();
        // Service-Mock: deleteEvent tut nichts
        doNothing().when(eventService).deleteEvent(id);

        // DELETE-Request ausführen und Status prüfen
        mockMvc.perform(delete(BASE + "/" + id))
                .andExpect(status().isNoContent());          // HTTP 204

        // Verifizieren, dass deleteEvent(id) aufgerufen wurde
        verify(eventService).deleteEvent(id);
    }

    @Test
    @DisplayName("GET /api/events mit Filterparametern → 200 + leere Liste")
    void testListEvents_WithFilter() throws Exception {
        // Service-Mock liefert eine leere Liste
        when(eventService.listEvents(any(FilterDto.class)))
                .thenReturn(Collections.emptyList());

        // GET mit Query-Parametern ausführen
        mockMvc.perform(get(BASE)
                        .param("weekday", "MONDAY")
                        .param("from", "2025-07-01T00:00:00Z"))
                .andExpect(status().isOk())                // HTTP 200
                .andExpect(content().json("[]"));          // leeres JSON-Array

        // Mit ArgumentCaptor prüfen, dass Controller den Filter korrekt übergibt
        ArgumentCaptor<FilterDto> fdCap = ArgumentCaptor.forClass(FilterDto.class);
        verify(eventService).listEvents(fdCap.capture());
        assertThat(fdCap.getValue().getWeekday()).isEqualTo("MONDAY");
        assertThat(fdCap.getValue().getFrom()).isEqualTo("2025-07-01T00:00:00Z");
    }
}

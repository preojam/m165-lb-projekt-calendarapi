package ch.schule165.calendar.controller;

import ch.schule165.calendar.dto.FilterDto;
import ch.schule165.calendar.model.Event;
import ch.schule165.calendar.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ch.schule165.calendar.controller.EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getEvents_withFilters_shouldReturnEvents() throws Exception {
        Event mockEvent = new Event();
        mockEvent.setName("Test Event");

        when(eventService.listEvents(any(FilterDto.class)))
                .thenReturn(List.of(mockEvent));

        mockMvc.perform(get("/events")
                        .param("weekday", "MONDAY", "TUESDAY")
                        .param("month", "6", "7")
                        .param("from", "2025-06-01T00:00:00Z")
                        .param("to", "2025-07-01T00:00:00Z")
                        .param("tag", "test")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Event"));
    }
}
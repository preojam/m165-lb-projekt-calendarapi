

package com.calendarapi.lbprojektm165calendarapi.SmokeTest;

import com.calendarapi.lbprojektm165calendarapi.controller.EventController;
import com.calendarapi.lbprojektm165calendarapi.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.calendarapi.lbprojektm165calendarapi.repository.EventRepository;
import org.springframework.http.MediaType;

    @SpringBootTest
    @AutoConfigureMockMvc
    class CalendarApiSmokeTest {

        @Autowired
        private EventController eventController;

        @Autowired
        private EventService eventService;

        @Autowired
        private MongoTemplate mongoTemplate;

        @Autowired
        private MockMvc mockMvc;

        @Test
        void contextLoads() {
            // Prüft, ob der ApplicationContext ohne Fehler lädt
        }

        @Test
        void eventControllerIsLoaded() {
            assertThat(eventController).isNotNull();
        }

        @Test
        void eventServiceIsLoaded() {
            assertThat(eventService).isNotNull();
        }

        @Test
        void mongoDbIsAvailable() {
            boolean exists = mongoTemplate.collectionExists("events");
            assertThat(exists).isTrue();
        }

        @Test
        void eventEndpointIsReachable() throws Exception {
            mockMvc.perform(get("/api/events")) // Passe den Pfad an, falls anders
                    .andExpect(status().isOk()); // Oder is2xxSuccessful(), falls du 204 o.ä. erwartest
        }

        @Autowired
        private EventRepository eventRepository;

        @Test
        void eventRepositoryIsLoaded() {
            assertThat(eventRepository).isNotNull();
        }
        @Test
        void mongoDbQueryWorks() {
            assertThat(eventRepository.findAll()).isNotNull();

        }

        @Test
        void createEventWithInvalidDataReturns400() throws Exception {
            mockMvc.perform(
                    org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                            .post("/api/events")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}") // Leerer Body → sollte 400 auslösen
            ).andExpect(status().isBadRequest());
        }




    }

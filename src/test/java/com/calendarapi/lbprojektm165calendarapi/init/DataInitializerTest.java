package com.calendarapi.lbprojektm165calendarapi.init;

import com.calendarapi.lbprojektm165calendarapi.model.Event;
import com.calendarapi.lbprojektm165calendarapi.repository.EventRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataInitializerTest {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private ApplicationContext ctx;

    @BeforeAll
    void setupAndSeed() throws Exception {
        // Vor allen Tests einmal löschen und Seeder ausführen
        eventRepo.deleteAll();
        CommandLineRunner runner = ctx.getBean("initData", CommandLineRunner.class);
        runner.run();
    }

    @Test
    void testDataIsInitialized() {
        // Nach dem ersten Seed müssen 6 Events da sein
        List<Event> all = eventRepo.findAll();
        assertThat(all).hasSize(6);
    }

    @Test
    void testContentOfSeededEvents() {
        // Prüfe, dass alle sechs erwarteten Titel vorhanden sind
        List<Event> all = eventRepo.findAll();
        assertThat(all)
                .extracting(Event::getTitle)
                .containsExactlyInAnyOrder(
                        "Sprint Planning",
                        "Team Retro",
                        "Marketing-Meeting",
                        "DevOps Check",
                        "Budget Review Q3",
                        "Customer Webinar"
                );
    }

    @Test
    void testIdempotenceOfInitializer() throws Exception {
        // Führt den Runner ein zweites und drittes Mal aus
        CommandLineRunner runner = ctx.getBean("initData", CommandLineRunner.class);
        runner.run();
        runner.run();
        // Da seeder deleteAll() + saveAll(6), bleibt es immer bei 6
        assertThat(eventRepo.findAll()).hasSize(6);
    }
}

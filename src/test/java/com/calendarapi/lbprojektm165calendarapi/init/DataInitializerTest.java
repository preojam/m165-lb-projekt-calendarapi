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

/**
 * Testklasse für die {@code DataInitializer}-Konfiguration im "dev"-Profil.
 * <p>
 * Stellt sicher, dass der CommandLineRunner zum Initialisieren der Beispieldaten
 * korrekt ausgeführt wird und erwartetes Verhalten zeigt:
 * </p>
 * <ul>
 *   <li>Daten werden einmalig und idempotent angelegt.</li>
 *   <li>Die korrekte Anzahl und Inhalt der Events ist gewährleistet.</li>
 * </ul>
 */
@SpringBootTest
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataInitializerTest {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private ApplicationContext ctx;

    /**
     * Löscht alle vorhandenen Events und startet den DataInitializer-Runner,
     * um die Seed-Daten einmalig vor allen Tests zu erzeugen.
     *
     * @throws Exception wenn das Ausführen des CommandLineRunner fehlschlägt
     */
    @BeforeAll
    void setupAndSeed() throws Exception {
        // Vor allen Tests einmal löschen
        eventRepo.deleteAll();
        // Runner aus dem Kontext holen und ausführen
        CommandLineRunner runner = ctx.getBean("initData", CommandLineRunner.class);
        runner.run();
    }

    /**
     * Verifiziert, dass nach dem Initialisieren genau sechs Events in der Datenbank vorhanden sind.
     */
    @Test
    void testDataIsInitialized() {
        // Nach dem ersten Seed müssen 6 Events da sein
        List<Event> all = eventRepo.findAll();
        assertThat(all).hasSize(6);
    }

    /**
     * Prüft, dass die Titel der erzeugten Events den sechs erwarteten Werten entsprechen.
     */
    @Test
    void testContentOfSeededEvents() {
        // Alle Events abrufen
        List<Event> all = eventRepo.findAll();
        // Titel extrahieren und vergleichen
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

    /**
     * Stellt die Idempotenz des Initializers sicher, indem er
     * mehrfach ausgeführt wird und weiterhin genau sechs Events bestehen bleiben.
     *
     * @throws Exception wenn das erneute Ausführen des CommandLineRunner fehlschlägt
     */
    @Test
    void testIdempotenceOfInitializer() throws Exception {
        // Runner erneut holen und zwei Mal ausführen
        CommandLineRunner runner = ctx.getBean("initData", CommandLineRunner.class);
        runner.run();
        runner.run();
        // Es bleibt bei 6 Events
        assertThat(eventRepo.findAll()).hasSize(6);
    }
}

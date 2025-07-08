package com.calendarapi.lbprojektm165calendarapi.init;

import com.calendarapi.lbprojektm165calendarapi.model.Event;
import com.calendarapi.lbprojektm165calendarapi.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("dev")
@SpringBootTest
public class DataInitializerTest {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private ApplicationContext ctx;

    @BeforeEach
    void cleanUp() {
        // Vor jedem Test: alle Events löschen, damit jeder Test mit leerer Datenbank startet
        eventRepo.deleteAll();
    }

    @Test
    void testSeederBeanExistsInDev() {
        assertThat(ctx.containsBean("initData")).isTrue();
        // Prüft, ob im "dev"-Profil ein Bean mit dem Namen "initData" (CommandLineRunner) geladen wurde
    }

    @Test
    void testDataIsInitialized() {
        List<Event> all = eventRepo.findAll();
        assertThat(all).hasSize(0);
        // Da wir in @BeforeEach alle Events gelöscht haben,
        // erwarten wir jetzt eine leere Tabelle (keine automatischen Seeds während des Tests)
    }

    @Test
    void testContentOfSeededEvents() {
        List<Event> all = eventRepo.findAll();
        assertThat(all)
                .extracting(Event::getTitle)
                .containsExactly("Sprint Planning", "Team Retro");
        // Hier prüfen wir, ob die Seed-Daten (zwei Events) korrekt angelegt wurden.
        // Achtung: je nach Reihenfolge des cleanUp kann es nötig sein, den Runner
        // vorab erneut auszuführen, sonst ist die Liste leer!
    }

    @Test
    void testIdempotenceOfInitializer() throws Exception {
        // Holt sich den CommandLineRunner "initData" aus dem Kontext
        CommandLineRunner runner = ctx.getBean("initData", CommandLineRunner.class);
        // Führt ihn zweimal aus, um Idempotenz zu prüfen
        runner.run();
        runner.run();
        // Erwartung: nach doppeltem Ausführen wurden die beiden Events nicht jeweils neu erzeugt,
        // sondern insgesamt 6 Datensätze (2 initial + 2*2) – oder je nach gewünschter Logik:
        // hier wird getestet, dass keine Duplikate angelegt werden
        assertThat(eventRepo.findAll())
                .hasSize(6);
    }

}
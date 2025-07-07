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
        eventRepo.deleteAll();
    }

    @Test
    void testSeederBeanExistsInDev() {
        // Der initData-Runner (Bean-Name = method name) muss vorhanden sein
        assertThat(ctx.containsBean("initData")).isTrue();
    }

    @Test
    void testDataIsInitialized() {
        List<Event> all = eventRepo.findAll();
        assertThat(all).hasSize(0);
    }

    @Test
    void testContentOfSeededEvents() {
        List<Event> all = eventRepo.findAll();
        assertThat(all)
                .extracting(Event::getTitle)
                .containsExactly("Sprint Planning", "Team Retro");
    }

    @Test
    void testIdempotenceOfInitializer() throws Exception {
        // Runner manuell abrufen und zweimal ausführen
        CommandLineRunner runner = ctx.getBean("initData", CommandLineRunner.class);
        runner.run();
        runner.run();
        assertThat(eventRepo.findAll())
                .hasSize(2);
    }

}

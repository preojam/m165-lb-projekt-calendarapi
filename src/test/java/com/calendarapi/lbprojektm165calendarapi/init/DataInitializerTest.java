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
    void setup() throws Exception {
        eventRepo.deleteAll();
        CommandLineRunner runner = ctx.getBean("initData", CommandLineRunner.class);
        runner.run();
    }

    @Test
    void testContentOfSeededEvents() {
        List<Event> all = eventRepo.findAll();
        assertThat(all)
                .extracting(Event::getTitle)
                .containsExactly("Sprint Planning", "Team Retro");
    }

    // … weitere Tests …
}

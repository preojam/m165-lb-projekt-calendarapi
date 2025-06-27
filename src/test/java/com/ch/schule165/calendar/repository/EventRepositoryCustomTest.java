package ch.schule165.calendar.repository;

import ch.schule165.calendar.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class EventRepositoryCustomTest {

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setup() {
        eventRepository.deleteAll();

        Event event1 = new Event();
        event1.setName("Yoga");
        event1.setDaysOfWeek(List.of("MONDAY", "WEDNESDAY"));
        event1.setMonths(List.of(6));
        event1.setStart(Instant.parse("2025-06-10T08:00:00Z"));
        event1.setTags(List.of("health"));

        Event event2 = new Event();
        event2.setName("Team Meeting");
        event2.setDaysOfWeek(List.of("TUESDAY"));
        event2.setMonths(List.of(7));
        event2.setStart(Instant.parse("2025-07-15T10:00:00Z"));
        event2.setTags(List.of("work"));

        eventRepository.saveAll(List.of(event1, event2));
    }

    @Test
    void findByFilters_shouldReturnMatchingEvents() {
        List<Event> result = eventRepository.findByFilters(
                List.of("MONDAY"),
                List.of(6),
                Instant.parse("2025-06-01T00:00:00Z"),
                Instant.parse("2025-06-30T23:59:59Z"),
                "health"
        );

        assertEquals(1, result.size());
        assertEquals("Yoga", result.get(0).getName());
    }
}
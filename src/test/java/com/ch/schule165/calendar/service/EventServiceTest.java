package ch.schule165.calendar.service;

import ch.schule165.calendar.dto.FilterDto;
import ch.schule165.calendar.model.Event;
import ch.schule165.calendar.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void listEvents_shouldForwardFiltersToRepository() {
        // Arrange
        FilterDto filter = new FilterDto(
                List.of("MONDAY", "TUESDAY"),
                List.of(5, 6),
                Instant.parse("2025-05-01T00:00:00Z"),
                Instant.parse("2025-07-01T00:00:00Z"),
                "birthday"
        );

        List<Event> mockEvents = List.of(new Event("Test"));
        when(eventRepository.findByFilters(
                anyList(), anyList(), any(), any(), anyString()
        )).thenReturn(mockEvents);

        // Act
        List<Event> result = eventService.listEvents(filter);

        // Assert
        assertEquals(mockEvents, result);
        verify(eventRepository).findByFilters(
                eq(filter.getWeekday()),
                eq(filter.getMonth()),
                eq(filter.getFrom()),
                eq(filter.getTo()),
                eq(filter.getTag())
        );
    }
}
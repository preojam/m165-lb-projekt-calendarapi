package com.calendarapi.lbprojektm165calendarapi.calendarapitests.unit;

import com.calendarapi.lbprojektm165calendarapi.dto.FilterDto;
import com.calendarapi.lbprojektm165calendarapi.model.Event;
import com.calendarapi.lbprojektm165calendarapi.repository.EventRepository;
import com.calendarapi.lbprojektm165calendarapi.service.EventService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository repository;

    @InjectMocks
    private EventService eventService;

    public EventServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listEvents_shouldCallRepositoryWithTitleContains() {
        // GIVEN
        FilterDto filter = new FilterDto();
        filter.setTitleContains("Meeting");

        // WHEN
        when(repository.findByFilters(filter)).thenReturn(Collections.emptyList());
        eventService.listEvents(filter);

        // THEN
        verify(repository, times(1)).findByFilters(filter);
    }
}
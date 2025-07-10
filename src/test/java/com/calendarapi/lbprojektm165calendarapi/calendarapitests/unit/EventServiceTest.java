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

/**
 * Unit-Test für die Klasse {@link EventService}.
 * Testet das Verhalten der listEvents-Methode in Verbindung mit dem Repository.
 */
class EventServiceTest {

    @Mock
    private EventRepository repository; // Wird durch Mockito simuliert

    @InjectMocks
    private EventService eventService; // Hier wird das Mock-Repository reingespritzt

    /**
     * Konstruktor initialisiert die Mocks.
     * Dies ersetzt die manuelle Initialisierung in jeder Testmethode.
     */
    public EventServiceTest() {
        MockitoAnnotations.openMocks(this); // Initialisiert alle Felder mit @Mock und @InjectMocks
    }

    /**
     * Testet, ob der Service die Methode findByFilters im Repository korrekt aufruft.
     * Der Filter enthält das Feld "titleContains", um nach Titeln zu suchen.
     */
    @Test
    void listEvents_shouldCallRepositoryWithTitleContains() {
        // GIVEN – Setup: Filter mit Titel "Meeting"
        FilterDto filter = new FilterDto();
        filter.setTitleContains("Meeting");

        // WHEN – Verhalten simulieren: Repository liefert leere Liste zurück
        when(repository.findByFilters(filter)).thenReturn(Collections.emptyList());

        // Aufruf der zu testenden Methode
        eventService.listEvents(filter);

        // THEN – Überprüfen, ob das Repository genau einmal mit dem Filter aufgerufen wurde
        verify(repository, times(1)).findByFilters(filter);
    }
}

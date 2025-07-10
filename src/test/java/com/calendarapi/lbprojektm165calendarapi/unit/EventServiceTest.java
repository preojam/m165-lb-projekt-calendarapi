package com.calendarapi.lbprojektm165calendarapi.unit;

import com.calendarapi.lbprojektm165calendarapi.dto.FilterDto;
import com.calendarapi.lbprojektm165calendarapi.repository.EventRepository;
import com.calendarapi.lbprojektm165calendarapi.service.EventService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.mockito.Mockito.*;

/**
 * Unit-Testklasse für {@link EventService}.
 * Testet die Interaktion mit dem {@link EventRepository} unter Verwendung von Mockito.
 * Ziel ist es sicherzustellen, dass der Service korrekt mit dem Repository kommuniziert.
 *
 * <p>Dieser Test prüft keine Datenbank oder Logik – er testet die Verbindung (Integration) zwischen
 * Service und Repository mit kontrolliertem Verhalten.</p>
 *
 * @author Chris
 */
class EventServiceTest {

    /**
     * Simuliertes Repository – stellt sicher, dass keine echte Datenbank verwendet wird.
     */
    @Mock
    private EventRepository repository;

    /**
     * Die getestete Service-Klasse mit automatisch injiziertem Mock-Repository.
     */
    @InjectMocks
    private EventService eventService;

    /**
     * Initialisiert die Mocks für diese Testklasse.
     * Diese Methode wird beim Erstellen der Testklasse aufgerufen, um die Felder zu verbinden.
     */
    public EventServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Testfall:
     * Prüft, ob die Methode {@code listEvents} im Service
     * das Repository korrekt mit dem gegebenen Filter aufruft.
     *
     * @given Ein {@link FilterDto} mit {@code titleContains = "Meeting"}
     * @when Die Methode {@code listEvents} aufgerufen wird
     * @then Das Repository wird genau einmal mit diesem Filter abgefragt
     */
    @Test
    void listEvents_shouldCallRepositoryWithTitleContains() {
        // GIVEN – Erstelle einen Filter mit einem Titelkriterium
        FilterDto filter = new FilterDto();
        filter.setTitleContains("Meeting");

        // WHEN – Definiere Verhalten des Mocks: Repository liefert leere Liste zurück
        when(repository.findByFilters(filter)).thenReturn(Collections.emptyList());

        // Ausführung der Methode
        eventService.listEvents(filter);

        // THEN – Verifiziere, dass das Repository genau einmal mit dem Filter aufgerufen wurde
        verify(repository, times(1)).findByFilters(filter);
    }
}

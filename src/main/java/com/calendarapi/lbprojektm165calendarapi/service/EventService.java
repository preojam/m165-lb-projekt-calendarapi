package com.calendarapi.lbprojektm165calendarapi.service;

import com.calendarapi.lbprojektm165calendarapi.model.Event;
import com.calendarapi.lbprojektm165calendarapi.dto.FilterDto;
import com.calendarapi.lbprojektm165calendarapi.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Der {@code EventService} stellt die zentrale Geschäftslogik für das Management von Kalender-Events bereit.
 * <p>
 * Er bietet Methoden zum Erstellen, Aktualisieren, Löschen, Abrufen sowie zum Filtern und Massenspeichern von Events.
 *
 * <p><strong>Hauptfunktionen:</strong></p>
 * <ul>
 *     <li>Speichern und Aktualisieren von Events</li>
 *     <li>Löschen von Events anhand ihrer ID</li>
 *     <li>Abrufen einzelner oder gefilterter Events</li>
 *     <li>Batch-Speicherung mehrerer Events</li>
 * </ul>
 *
 * @author Arvin
 */
@Service
public class EventService {

    /** Zugriff auf die Datenbank für Event-Entitäten */
    private final EventRepository repository;

    /**
     * Konstruktor mit Dependency Injection des Repositories.
     *
     * @param repository die Repository-Instanz zum Zugriff auf Event-Daten
     */
    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    /**
     * Legt ein neues Event in der Datenbank an.
     *
     * @param event das zu speichernde Event-Objekt
     * @return das gespeicherte Event mit generierter ID
     */
    public Event createEvent(Event event) {
        return repository.save(event);
    }

    /**
     * Aktualisiert ein bestehendes Event. Falls das Event nicht existiert, wird es neu angelegt.
     *
     * @param event das zu aktualisierende Event-Objekt
     * @return das aktualisierte (oder neu angelegte) Event
     */
    public Event updateEvent(Event event) {
        // Optional: prüfen, ob das Event existiert
        // repository.findById(event.getId()).orElseThrow(...)
        return repository.save(event);
    }

    /**
     * Löscht ein Event anhand seiner eindeutigen ID.
     *
     * @param id die ID des zu löschenden Events
     */
    public void deleteEvent(String id) {
        repository.deleteById(id);
    }

    /**
     * Gibt ein einzelnes Event anhand seiner ID zurück.
     *
     * @param id die eindeutige ID des gesuchten Events
     * @return das gefundene Event-Objekt
     * @throws IllegalArgumentException wenn kein Event mit der angegebenen ID existiert
     */
    public Event getById(String id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Event nicht gefunden: " + id)
                );
    }

    /**
     * Listet alle Events basierend auf den angegebenen Filterkriterien.
     *
     * @param filter das {@link FilterDto}-Objekt mit Kriterien wie Datum, Titel oder Wochentag
     * @return eine Liste von Events, die den Filterkriterien entsprechen
     */
    public List<Event> listEvents(FilterDto filter) {
        return repository.findByFilters(filter);
    }

    /**
     * Speichert mehrere Events gleichzeitig (Batch-Speicherung).
     *
     * @param events eine Liste von Events, die gespeichert werden sollen
     * @return eine Liste der gespeicherten Events
     */
    public List<Event> saveAll(List<Event> events) {
        return repository.saveAll(events);
    }
}
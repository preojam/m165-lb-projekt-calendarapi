package com.calendarapi.lbprojektm165calendarapi.service;

import com.calendarapi.lbprojektm165calendarapi.model.Event;
import com.calendarapi.lbprojektm165calendarapi.repository.EventRepository;
import com.calendarapi.lbprojektm165calendarapi.dto.FilterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository repository;

    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    /** Legt ein neues Event an */
    public Event createEvent(Event event) {
        return repository.save(event);
    }

    /** Updated ein bestehendes Event */
    public Event updateEvent(Event event) {
        // Optional: prüfen, ob das Event existiert:
        // repository.findById(event.getId()).orElseThrow(...)
        return repository.save(event);
    }

    /** Löscht ein Event über seine ID */
    public void deleteEvent(String id) {
        repository.deleteById(id);
    }

    /** Liest ein einzelnes Event */
    public Event getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event nicht gefunden: " + id));
    }

    public List<Event> listEvents(FilterDto filter) {
        return repository.findByFilters(filter);
    }

    /** Speichert eine Liste von Event-Objekten in der MongoDB-Datenbank*/
    public List<Event> saveAll(List<Event> events) {
        return repository.saveAll(events); //gibt die gespeicherten Daten zurück (inkl. IDs)
    }
}

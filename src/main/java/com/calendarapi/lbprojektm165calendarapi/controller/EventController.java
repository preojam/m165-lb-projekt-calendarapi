package com.calendarapi.lbprojektm165calendarapi.controller;

import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import com.calendarapi.lbprojektm165calendarapi.model.Event;
import com.calendarapi.lbprojektm165calendarapi.service.EventService;
import com.calendarapi.lbprojektm165calendarapi.dto.FilterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.cronutils.model.CronType.QUARTZ; // spring hat 6 stellig und quartz 7 stellig **900?

/**
 * REST-Controller für die Verwaltung von Events.
 * Bietet HTTP-Endpunkte zum Erstellen, Bearbeiten, Löschen und Filtern von Kalenderereignissen.
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService; // Service-Klasse zur Event-Verarbeitung
    private final CronParser cronParser; // Parser zur Validierung von Cron-Ausdrücken

    /**
     * Konstruktor mit Dependency Injection.
     * Initialisiert den CronParser mit QUARTZ-Spezifikation (7-stellig).
     * @param eventService Service-Schicht für Events
     */
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
        CronDefinition definition = CronDefinitionBuilder.instanceDefinitionFor(QUARTZ); // gewechselt zu spring
        this.cronParser = new CronParser(definition);
    }

    /**
     * Erstellt ein einzelnes Event.
     * @param event Das zu erstellende Event
     * @return Das gespeicherte Event mit generierter ID
     */
    @PostMapping
    public Event create(@RequestBody Event event) {
        validateCron(event.getCron()); // Cron-String wird überprüft
        return eventService.createEvent(event); // Event wird gespeichert
    }

    /**
     * Aktualisiert ein bestehendes Event anhand seiner ID.
     * @param id ID des zu aktualisierenden Events
     * @param event Event-Daten, die gespeichert werden sollen
     * @return Aktualisiertes Event
     */
    @PutMapping("/{id}") // id ist platzhalter
    public Event update(@PathVariable String id, @RequestBody Event event) {
        validateCron(event.getCron()); // Cron-String wird überprüft
        event.setId(id); // ID aus URL wird ins Event gesetzt
        return eventService.updateEvent(event);
    }

    /**
     * Einfacher REST-Endpunkt der auf die Anfrage /hello einen Text "Hello World!" ausgibt.
     * @return Begrüßungstext
     */
    @GetMapping("/hello")
    public String hello(){
        return "Hello World!";
    }

    /**
     * Gibt ein Event anhand seiner ID zurück.
     * @param id Die ID des gesuchten Events
     * @return Event-Objekt
     */
    @GetMapping("/{id}")
    public Event getById(@PathVariable String id) {
        return eventService.getById(id); // Sucht Event über Service
    }

    /**
     * Löscht ein Event anhand seiner ID.
     * Gibt keinen Body zurück, sondern nur Status 204.
     * @param id Die ID des zu löschenden Events
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Gibt nur 204 No Content zurück
    public void delete(@PathVariable String id) {
        eventService.deleteEvent(id); // Löscht Event über Service
    }

    /**
     * Validiert einen Cron-Ausdruck im Kontext eines Spring-Cron-Formats.
     * Wenn der Ausdruck ungültig ist, wird eine 400-BAD_REQUEST zurückgegeben.
     * @param cronPattern Der zu überprüfende Cron-Ausdruck
     */
    private void validateCron(String cronPattern) {
        try {
            cronParser.parse(cronPattern).validate(); // validate() prüft, ob das Pattern syntaktisch und semantisch korrekt
        } catch (Exception e) { // Falls die Validierung fehlschlägt, fängt es die Exception ab
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, // Der Client bekommt den Fehlercode 400
                    "Ungültiges Cron-Pattern: " + cronPattern // Angezeigte Error Message
            );
        }
    }

    /**
     * REST Mapping Endpunkt im Controller.
     * Erstellt mehrere Events auf einmal (Batch-Insertion).
     * @param events Liste von Events, die gespeichert werden sollen
     * @return Liste der gespeicherten Events
     */
    @PostMapping("/batch") // Der Endpunkt reagiert auf HTTP POST-Anfragen an /api/events/batch
    public List<Event> createEvents(@RequestBody List<Event> events) { // empfängt eine Liste von einem JSON-Body
        return eventService.saveAll(events); // gibt die Liste an den Service, der sie mit saveAll in die Datenbank einschreibt
    }

    /**
     * Listet Events mit optionalen Filterkriterien.
     * Unterstützt Filter wie Wochentag, Monat, Zeitfenster, Tag, Titel-Suchwort und Datumsbereich.
     * @param weekday Optionaler Filter für Wochentag
     * @param month Optionaler Filter für Monat
     * @param from Optionaler Zeit-Startfilter ("08:00")
     * @param to Optionaler Zeit-Endfilter ("12:00")
     * @param tag Optionaler Tag-Filter
     * @param titleContains Filtert nach Titeln, die den angegebenen String enthalten
     * @param dateFrom Startdatum des Datumsbereichs
     * @param dateTo Enddatum des Datumsbereichs
     * @return Gefilterte Liste von Events
     */
    @GetMapping
    public List<Event> listEvents(
            @RequestParam(required = false) String weekday,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String titleContains,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo
    ) {
        FilterDto filter = new FilterDto(); // Neues Filter-Objekt
        filter.setWeekday(weekday); // Übergabe aller Filter
        filter.setMonth(month);
        filter.setFrom(from);
        filter.setTo(to);
        filter.setTag(tag);
        filter.setTitleContains(titleContains);
        filter.setDateFrom(dateFrom);
        filter.setDateTo(dateTo);
        return eventService.listEvents(filter); // Rückgabe der gefilterten Liste
    }
}

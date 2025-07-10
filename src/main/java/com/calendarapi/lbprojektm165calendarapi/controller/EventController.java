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
 * Der {@code EventController} stellt REST-Endpunkte zur Verwaltung von Kalender-Events bereit.
 * Dazu gehören das Erstellen, Bearbeiten, Löschen und Filtern von Ereignissen.
 * <p>
 * Die Validierung von Cron-Ausdrücken erfolgt mithilfe des Quartz-Standards.
 * </p>
 *
 * @author Chris
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService; // Service-Klasse zur Event-Verarbeitung
    private final CronParser cronParser;     // Parser zur Validierung von Cron-Ausdrücken

    /**
     * Konstruktor mit Dependency Injection.
     * Initialisiert den CronParser mit QUARTZ-Spezifikation (7-stellig).
     *
     * @param eventService Service-Schicht für Events
     */
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
        CronDefinition definition = CronDefinitionBuilder.instanceDefinitionFor(QUARTZ);
        this.cronParser = new CronParser(definition);
    }

    /**
     * Erstellt ein einzelnes Event.
     *
     * @param event Das zu erstellende Event
     * @return Das gespeicherte Event mit generierter ID
     */
    @PostMapping
    public Event create(@RequestBody Event event) {
        validateCron(event.getCron());
        return eventService.createEvent(event);
    }

    /**
     * Aktualisiert ein bestehendes Event anhand seiner ID.
     *
     * @param id    ID des zu aktualisierenden Events
     * @param event Event-Daten, die gespeichert werden sollen
     * @return Das aktualisierte Event
     */
    @PutMapping("/{id}")
    public Event update(@PathVariable String id, @RequestBody Event event) {
        validateCron(event.getCron());
        event.setId(id);
        return eventService.updateEvent(event);
    }

    /**
     * Test-Endpunkt zur Überprüfung, ob die API erreichbar ist.
     *
     * @return Der Text "Hello World!"
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    /**
     * Gibt ein Event anhand seiner ID zurück.
     *
     * @param id Die ID des gesuchten Events
     * @return Das gefundene Event-Objekt
     */
    @GetMapping("/{id}")
    public Event getById(@PathVariable String id) {
        return eventService.getById(id);
    }

    /**
     * Löscht ein Event anhand seiner ID.
     *
     * @param id Die ID des zu löschenden Events
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        eventService.deleteEvent(id);
    }

    /**
     * Validiert einen Cron-Ausdruck im Kontext eines QUARTZ-Cron-Formats.
     * Falls der Ausdruck ungültig ist, wird ein HTTP 400 (Bad Request) geworfen.
     *
     * @param cronPattern Der zu überprüfende Cron-Ausdruck
     * @throws ResponseStatusException wenn das Cron-Pattern ungültig ist
     */
    private void validateCron(String cronPattern) {
        try {
            cronParser.parse(cronPattern).validate();
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ungültiges Cron-Pattern: " + cronPattern
            );
        }
    }

    /**
     * Erstellt mehrere Events in einem einzigen Request.
     *
     * @param events Liste von Event-Objekten
     * @return Liste der gespeicherten Events
     */
    @PostMapping("/batch")
    public List<Event> createEvents(@RequestBody List<Event> events) {
        return eventService.saveAll(events);
    }

    /**
     * Listet Events mit optionalen Filterkriterien.
     *
     * @param weekday        Optionaler Filter für Wochentag ("MONDAY")
     * @param month          Optionaler Filter für Monat ("1,2,12")
     * @param from           Optionaler Startzeitpunkt (ISO 8601)
     * @param to             Optionaler Endzeitpunkt (ISO 8601)
     * @param tag            Optionaler Tag-Filter ("Privat")
     * @param titleContains  Optionaler Teilstring, der im Titel enthalten sein soll
     * @param dateFrom       Optionales Startdatum im Format yyyy-MM-dd
     * @param dateTo         Optionales Enddatum im Format yyyy-MM-dd
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
        FilterDto filter = new FilterDto();
        filter.setWeekday(weekday);
        filter.setMonth(month);
        filter.setFrom(from);
        filter.setTo(to);
        filter.setTag(tag);
        filter.setTitleContains(titleContains);
        filter.setDateFrom(dateFrom);
        filter.setDateTo(dateTo);
        return eventService.listEvents(filter);
    }
}

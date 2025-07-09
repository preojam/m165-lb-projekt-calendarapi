package com.calendarapi.lbprojektm165calendarapi.controller;

import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import com.calendarapi.lbprojektm165calendarapi.model.Event;
import com.calendarapi.lbprojektm165calendarapi.service.EventService;
import com.calendarapi.lbprojektm165calendarapi.dto.FilterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

import static com.cronutils.model.CronType.QUARTZ;

@RestController
@RequestMapping("/api/events")
public class
EventController {

    private final EventService eventService;
    private final CronParser cronParser;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
        CronDefinition definition = CronDefinitionBuilder.instanceDefinitionFor(QUARTZ);
        this.cronParser = new CronParser(definition);
    }

    @PostMapping
    public Event create(@RequestBody Event event) {
        validateCron(event.getCron());
        return eventService.createEvent(event);
    }

    @PutMapping("/{id}") //id ist platzhalter
    public Event update(@PathVariable String id, @RequestBody Event event) {
        validateCron(event.getCron());
        event.setId(id);
        return eventService.updateEvent(event);
    }

    @GetMapping
    public List<Event> listEvents(
            @RequestParam(required = false) String weekday,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String tag
    ) {
        FilterDto filter = new FilterDto();
        filter.setWeekday(weekday);
        filter.setMonth(month);
        filter.setFrom(from);
        filter.setTo(to);
        filter.setTag(tag);
        return eventService.listEvents(filter);
    }

    //einfacher REST Endpunkt der auf die Anfrage /hello einen Text "Hello World!" ausgibt.
    @GetMapping("/hello")
    public String hello(){
        return "Hello World!";
    }

    @GetMapping("/{id}")
    public Event getById(@PathVariable String id) {
        return eventService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        eventService.deleteEvent(id);
    }

    /**Validiert dient der Validierung von Cron Ausdrücken im kontext einem Spring Kontext*/
    private void validateCron(String cronPattern) {
        try {
            cronParser.parse(cronPattern).validate(); //validate() prüft, ob das Pattern syntaktisch und semantisch korrekt
        } catch (Exception e) { //Falls die Validierung fehlschlägt fängt es die exception ab
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, //Der client bekommt den Fehlercode 400
                    "Ungültiges Cron-Pattern: " + cronPattern //angezeigte error message
            );
        }
    }

    /**Rest Mapping Endpunkt im Controller */
    @PostMapping("/batch") //Der Endpunkt reagiert auf HTTP POST-Anfragen an /api/events/batch
    public List<Event> createEvents(@RequestBody List<Event> events) { //empfängt eine Liste von einem Json Body
        return eventService.saveAll(events); //gibt die Liste an den Service der sie mit SaveAll in die Datenbank einschreibt
    }
    @GetMapping
    public List<Event> listEvents(
            @RequestParam(required = false) String weekday,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String titleContains  // 👈 NEU
    ) {
        FilterDto filter = new FilterDto();
        filter.setWeekday(weekday);
        filter.setMonth(month);
        filter.setFrom(from);
        filter.setTo(to);
        filter.setTag(tag);
        filter.setTitleContains(titleContains);
        return eventService.listEvents(filter);
    }
}


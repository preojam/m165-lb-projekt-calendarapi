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
@RequestMapping("/events")
public class EventController {

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

    @PutMapping("/{id}")
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

    @GetMapping("/{id}")
    public Event getById(@PathVariable String id) {
        return eventService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        eventService.deleteEvent(id);
    }

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

}

//hallo grützi
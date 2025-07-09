package com.calendarapi.lbprojektm165calendarapi.service;

import com.calendarapi.lbprojektm165calendarapi.model.Event;
import com.calendarapi.lbprojektm165calendarapi.dto.FilterDto;
import com.calendarapi.lbprojektm165calendarapi.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
public class EventService {

    private final EventRepository repository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public EventService(EventRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    /** Legt ein neues Event an */
    public Event createEvent(Event event) {
        return repository.save(event);
    }

    /** Updated ein bestehendes Event */
    public Event updateEvent(Event event) {
        // Optional: prüfen, ob das Event existiert
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
                .orElseThrow(() ->
                        new IllegalArgumentException("Event nicht gefunden: " + id)
                );
    }

    /**
     * Listet Events nach den im FilterDto angegebenen Kriterien.
     * Verwendet MongoTemplate für flexible Abfragen.
     */
    public List<Event> listEvents(FilterDto filter) {
        Query mongoQuery = new Query();
        Criteria mongoCriteria = new Criteria();

        // 1) Wochentage filtern, wenn Parameter gesetzt. csv = comma separated values
        String weekdayCsv = filter.getWeekday();
        if (weekdayCsv != null && !weekdayCsv.isBlank()) {
            List<String> weekdayList = Arrays.stream(weekdayCsv.split(","))
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .toList();
            mongoCriteria.and("daysOfWeek").in(weekdayList);
        }

        // 2) Start-Zeit ab (from)
        Instant fromInstant = filter.getFrom();
        if (fromInstant != null) {
            mongoCriteria.and("start").gte(fromInstant);
        }

        // 3) End-Zeit bis (to)
        Instant toInstant = filter.getTo();
        if (toInstant != null) {
            mongoCriteria.and("end").lte(toInstant);
        }

        // 4) Monate
        List<Integer> monthList = filter.getMonth();
        if (monthList != null && !monthList.isEmpty()) {
            mongoCriteria.and("months").in(monthList);
        }

        // 5) Schlagwort-Filter
        String tagFilter = filter.getTag();
        if (tagFilter != null && !tagFilter.isBlank()) {
            mongoCriteria.and("tags").in(tagFilter);
        }

        mongoQuery.addCriteria(mongoCriteria);
        return mongoTemplate.find(mongoQuery, Event.class);
    }

    /**
     * Speichert mehrere Events auf einmal.
     * Wird z.B. vom /api/events/batch-Endpoint genutzt.
     */
    public List<Event> saveAll(List<Event> events) {
        return repository.saveAll(events);
    }
}

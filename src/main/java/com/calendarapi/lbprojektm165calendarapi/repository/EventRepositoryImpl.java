package com.calendarapi.lbprojektm165calendarapi.repository;

import com.calendarapi.lbprojektm165calendarapi.dto.FilterDto;
import com.calendarapi.lbprojektm165calendarapi.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Implementierung von benutzerdefinierten Abfragen für Events.
 */
public class EventRepositoryImpl implements EventRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate; // Ermöglicht direkte Abfragen auf MongoDB

    /**
     * Findet Events basierend auf den gegebenen Filterkriterien.
     *
     * @param filter das Filterobjekt, das verschiedene Kriterien wie Wochentage, Monate, Zeiträume etc. enthält
     * @return Liste von Events, die den Filterkriterien entsprechen
     */
    @Override
    public List<Event> findByFilters(FilterDto filter) {
        Query query = new Query(); // Startet leere Query, Kriterien werden dynamisch hinzugefügt

        // Filter nach Wochentagen (z. B. ["Monday", "Tuesday"])
        if (filter.getWeekday() != null && !filter.getWeekday().isEmpty()) {
            query.addCriteria(Criteria.where("weekday").in(filter.getWeekday()));
        }

        // Filter nach Monaten (z. B. ["January", "February"])
        if (filter.getMonth() != null && !filter.getMonth().isEmpty()) {
            query.addCriteria(Criteria.where("month").in(filter.getMonth()));
        }

        // Filter nach Zeitraum: von - bis (bezieht sich auf "startTime")
        if (filter.getFrom() != null && filter.getTo() != null) {
            // Wenn sowohl "von" als auch "bis" gesetzt sind
            query.addCriteria(Criteria.where("startTime").gte(filter.getFrom()).lte(filter.getTo()));
        } else if (filter.getFrom() != null) {
            // Nur "von" ist gesetzt
            query.addCriteria(Criteria.where("startTime").gte(filter.getFrom()));
        } else if (filter.getTo() != null) {
            // Nur "bis" ist gesetzt
            query.addCriteria(Criteria.where("startTime").lte(filter.getTo()));
        }

        // Filter nach einem spezifischen Tag (z. B. "work", "birthday")
        if (filter.getTag() != null && !filter.getTag().isBlank()) {
            query.addCriteria(Criteria.where("tag").is(filter.getTag()));
        }

        // Filter nach Titel (case-insensitive, enthält Teilwort)
        if (filter.getTitleContains() != null && !filter.getTitleContains().isBlank()) {
            query.addCriteria(
                    Criteria.where("title")
                            .regex(".*" + Pattern.quote(filter.getTitleContains()) + ".*", "i") // "i" = ignore case
            );
        }

        // Erstellt ein leeres Mongo-Query-Filterobjekt (Criteria aus org.springframework.data.mongodb.core.query)
        Criteria c = new Criteria();

        // Wenn ein Startdatum im Filter gesetzt ist
        if (filter.getDateFrom() != null) {
            Instant from = filter.getDateFrom()
                    .atStartOfDay(ZoneOffset.UTC) // Beginn des Tages, z. B. 2025-08-01T00:00Z (UTC)
                    .toInstant(); // Umwandlung in Instant (für Vergleich mit Event-Startzeit)
            c = c.and("start").gte(from); // Filter: Events, die am/vor oder nach diesem Zeitpunkt starten
        }

        // Wenn ein Enddatum im Filter gesetzt ist
        if (filter.getDateTo() != null) {
            Instant to = filter.getDateTo()
                    .plusDays(1) // Einen Tag hinzufügen, um den kompletten Endtag abzudecken
                    .atStartOfDay(ZoneOffset.UTC) // 00:00 Uhr des nächsten Tages (UTC)
                    .toInstant(); // Umwandlung in Instant (für Vergleich mit Event-Endzeit)
            c = c.and("end").lte(to); // Filter: Events, die am/vor diesem Zeitpunkt enden
        }

        // Führt die Abfrage aus und gibt eine Liste passender Events zurück
        return mongoTemplate.find(query, Event.class);
    }
}

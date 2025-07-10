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
 *
 * <p>Diese Klasse definiert die dynamische Filterlogik zur Suche nach Events basierend
 * auf verschiedenen Kriterien wie Titel, Zeiträumen, Wochentagen, Monaten und Tags.</p>
 *
 * @author Ricardo
 */
public class EventRepositoryImpl implements EventRepositoryCustom {

    /**
     * {@link MongoTemplate} wird verwendet, um direkte Abfragen auf die MongoDB zu ermöglichen.
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Findet Events, die den gegebenen Filterkriterien entsprechen.
     *
     * @param filter das Filterobjekt {@link FilterDto}, das folgende Felder enthalten kann:
     *               <ul>
     *                 <li>{@code weekday} – Liste von Wochentagen</li>
     *                 <li>{@code month} – Liste von Monatsnamen</li>
     *                 <li>{@code from}/{@code to} – Start- und Endzeitraum (basierend auf {@code startTime})</li>
     *                 <li>{@code tag} – einzelnes Tag</li>
     *                 <li>{@code titleContains} – Teilsuche im Titel (case-insensitive)</li>
     *                 <li>{@code dateFrom}/{@code dateTo} – Zeitraumfilter auf {@code start} und {@code end}</li>
     *               </ul>
     * @return Liste von {@link Event}-Objekten, die den Kriterien entsprechen
     */
    @Override
    public List<Event> findByFilters(FilterDto filter) {
        Query query = new Query();

        // Wochentage filtern
        if (filter.getWeekday() != null && !filter.getWeekday().isEmpty()) {
            query.addCriteria(Criteria.where("daysOfWeek").in(filter.getWeekday()));
        }

        // Monate filtern
        if (filter.getMonth() != null && !filter.getMonth().isEmpty()) {
            query.addCriteria(Criteria.where("months").in(filter.getMonth()));
        }

        // Startzeit-Zeitraum (optional)
        if (filter.getFrom() != null && filter.getTo() != null) {
            query.addCriteria(Criteria.where("start").gte(filter.getFrom()).lte(filter.getTo()));
        } else if (filter.getFrom() != null) {
            query.addCriteria(Criteria.where("start").gte(filter.getFrom()));
        } else if (filter.getTo() != null) {
            query.addCriteria(Criteria.where("start").lte(filter.getTo()));
        }

        // Tag (einzelnes Schlagwort)
        if (filter.getTag() != null && !filter.getTag().isBlank()) {
            query.addCriteria(Criteria.where("tags").is(filter.getTag()));
        }

        // Titel enthält (case-insensitive)
        if (filter.getTitleContains() != null && !filter.getTitleContains().isBlank()) {
            query.addCriteria(
                    Criteria.where("title")
                            .regex(".*" + Pattern.quote(filter.getTitleContains()) + ".*", "i")
            );
        }

        // Optionales Datumskriterium für start und end
        Criteria dateCriteria = new Criteria();

        boolean hasDateCriteria = false;

        if (filter.getDateFrom() != null) {
            Instant from = filter.getDateFrom().atStartOfDay(ZoneOffset.UTC).toInstant();
            dateCriteria = dateCriteria.and("start").gte(from);
            hasDateCriteria = true;
        }

        if (filter.getDateTo() != null) {
            Instant to = filter.getDateTo().plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
            dateCriteria = dateCriteria.and("end").lte(to);
            hasDateCriteria = true;
        }

        // Nur hinzufügen, wenn mindestens ein Datumswert vorhanden ist
        if (hasDateCriteria) {
            query.addCriteria(dateCriteria);
        }

        return mongoTemplate.find(query, Event.class);
    }
}

package com.calendarapi.lbprojektm165calendarapi.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

/**
 * Repräsentiert ein Kalenderevent in der MongoDB-Collection "events".
 *
 * <p>Diese Klasse wird verwendet, um wiederkehrende oder einmalige Termine
 * inklusive Metadaten wie Tags, Wochentage und Cron-Pattern zu speichern.</p>
 *
 * @author Ricardo Cardoso
 */
@Document(collection = "events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    /**
     * Die eindeutige ID des Events (wird von MongoDB generiert).
     */
    @Id
    private String id;

    /**
     * Der Titel des Events.
     */
    private String title;

    /**
     * Eine Beschreibung des Events.
     */
    private String description;

    /**
     * Der Startzeitpunkt des Events im ISO-Format (UTC).
     */
    private Instant start;

    /**
     * Der Endzeitpunkt des Events im ISO-Format (UTC).
     */
    private Instant end;

    /**
     * Ein optionales Cron-Pattern zur Definition wiederkehrender Events.
     * <p>Beispiel: {@code "0 0 12 * * ?"}</p>
     */
    private String cron;

    /**
     * Eine Liste von Schlagwörtern (Tags), um Events zu kategorisieren oder filtern zu können.
     */
    private List<String> tags;

    /**
     * Wochentage, an denen das Event stattfindet.
     * <p>Beispiel: {@code ["MONDAY", "WEDNESDAY"]}</p>
     */
    private List<String> daysOfWeek;

    /**
     * Fester Tag im Monat, an dem das Event stattfindet (z.B. der 15.).
     */
    private Integer dayOfMonth;

    /**
     * Liste von Monaten, in denen das Event stattfinden soll.
     * <p>Beispiel: {@code [1, 6, 12]} für Januar, Juni und Dezember.</p>
     */
    private List<Integer> months;
}

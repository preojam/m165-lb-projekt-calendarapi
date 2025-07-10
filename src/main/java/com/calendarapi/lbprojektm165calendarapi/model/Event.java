package com.calendarapi.lbprojektm165calendarapi.model;

import com.fasterxml.jackson.core.JsonToken;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

/**
 * Repräsentiert ein Event-Dokument in der MongoDB-Collection "events".
 */
@Document(collection = "events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    // Getter & Setter für id
    @Id
    private String id;

    // Getter & Setter für title
    private String title;
    // Getter & Setter für description
    private String description;
    // Getter & Setter für start
    private Instant start;
    // Getter & Setter für end
    private Instant end;
    // Getter & Setter für cron
    private String cron;               // für das Cron-Pattern
    // Getter & Setter für tags
    private List<String> tags;         // für Tags
    // Getter & Setter für daysOfWeek
    private List<String> daysOfWeek;   // für Filter
    // Getter & Setter für dayOfMonth
    private Integer dayOfMonth;
    // Getter & Setter für months
    private List<Integer> months;

    /**
     * Vollständiger Konstruktor der Event-Klasse, um alle Attribute zu initialisieren.
     *
     * @param title       Titel des Events
     * @param description Beschreibung des Events
     * @param start       Startzeitpunkt des Events (UTC)
     * @param end         Endzeitpunkt des Events (UTC)
     * @param cron        Cron-Ausdruck für Wiederholungsmuster
     * @param tags        Schlagwörter zur Kategorisierung
     * @param daysOfWeek  Liste von Wochentagen, an denen das Event stattfindet
     * @param dayOfMonth  Fester Tag im Monat
     * @param months      Liste von Monaten (z. B. [1, 6, 12])
     */
    public Event(String title, String description,
                 Instant start, Instant end,
                 String cron,
                 List<String> tags,
                 List<String> daysOfWeek,
                 Integer dayOfMonth,
                 List<Integer> months) {
        this.title = title;               // Titel des Events
        this.description = description;   // Beschreibung des Events
        this.start = start;               // Startzeitpunkt (Instant = UTC-Zeit)
        this.end = end;                   // Endzeitpunkt
        this.cron = cron;                 // Cron-Ausdruck für Wiederholungsmuster
        this.tags = tags;                 // Schlagwörter zur Kategorisierung
        this.daysOfWeek = daysOfWeek;     // Liste von Wochentagen, an denen das Event stattfindet
        this.dayOfMonth = dayOfMonth;     // Fester Tag im Monat (z. B. 15 für den 15.)
        this.months = months;             // Liste von Monaten (z. B. [1, 6, 12])
    }

    /**
     * Gibt die ID des Events zurück.
     */
    public String getId() {
        return id;
    }

    /**
     * Setzt die ID des Events.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gibt die Liste der Monate zurück.
     */
    public List<Integer> getMonths() {
        return months;
    }

    /**
     * Setzt die Liste der Monate.
     */
    public void setMonths(List<Integer> months) {
        this.months = months;
    }

    /**
     * Gibt den Titel des Events zurück.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setzt den Titel des Events.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gibt die Beschreibung des Events zurück.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setzt die Beschreibung des Events.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gibt den Startzeitpunkt zurück.
     */
    public Instant getStart() {
        return start;
    }

    /**
     * Setzt den Startzeitpunkt.
     */
    public void setStart(Instant start) {
        this.start = start;
    }

    /**
     * Gibt den Endzeitpunkt zurück.
     */
    public Instant getEnd() {
        return end;
    }

    /**
     * Setzt den Endzeitpunkt.
     */
    public void setEnd(Instant end) {
        this.end = end;
    }

    /**
     * Gibt den Cron-Ausdruck zurück.
     */
    public String getCron() {
        return cron;
    }

    /**
     * Setzt den Cron-Ausdruck.
     */
    public void setCron(String cron) {
        this.cron = cron;
    }

    /**
     * Gibt die Liste der Tags zurück.
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Setzt die Liste der Tags.
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Gibt die Liste der Wochentage zurück.
     */
    public List<String> getDaysOfWeek() {
        return daysOfWeek;
    }

    /**
     * Setzt die Liste der Wochentage.
     */
    public void setDaysOfWeek(List<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    /**
     * Gibt den Tag des Monats zurück.
     */
    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    /**
     * Setzt den Tag des Monats.
     */
    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

}

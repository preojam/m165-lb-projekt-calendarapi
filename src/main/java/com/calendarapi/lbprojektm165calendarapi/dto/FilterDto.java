package com.calendarapi.lbprojektm165calendarapi.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object (DTO) zur Übertragung von Filterparametern für Event-Abfragen.
 * Diese Klasse wird verwendet, um HTTP-Query-Parameter in typsichere Java-Werte zu überführen,
 * damit Events gezielt gefiltert werden können.
 *
 * Unterstützte Filter:
 * - Wochentage
 * - Monate
 * - Zeiträume (von/bis)
 * - Textsuche im Titel
 * - Tags (Kategorien)
 * - Datum von/bis
 *
 * <p>Wird hauptsächlich vom {@code EventController} verwendet.</p>
 *
 * @author Chris
 */
public class FilterDto {

    // Rohdaten-String für Wochentage ("MONDAY,TUESDAY")
    private String weekday;

    // Liste von Monaten (1 = Januar, 12 = Dezember)
    private List<Integer> month;

    // Start- und Endzeitpunkt (ISO 8601)
    private Instant from;
    private Instant to;

    // Kategorie-Tag ("Privat", "Arbeit")
    private String tag;

    // Teilwort-Suche im Titel
    private String titleContains;

    // Optionaler Datumsbereich (yyyy-MM-dd)
    private LocalDate dateFrom;
    private LocalDate dateTo;

    /**
     * Setzt die Wochentage als CSV-String ("MONDAY,TUESDAY").
     *
     * @param weekdaysCsv Kommagetrennte Liste von Wochentagen
     */
    public void setWeekday(String weekdaysCsv) {
        this.weekday = weekdaysCsv;
    }

    /**
     * Setzt die Monate als CSV-String ("1,3,12") und konvertiert sie in Integer.
     *
     * @param monthsCsv Kommagetrennte Liste von Monatszahlen (1–12)
     */
    public void setMonth(String monthsCsv) {
        this.month = (monthsCsv == null || monthsCsv.isBlank())
                ? Collections.emptyList()
                : List.of(monthsCsv.split(","))
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    /**
     * Setzt den Startzeitpunkt für Zeitfilterung.
     * Bei ungültigem oder leerem Wert wird {@code null} gesetzt.
     *
     * @param fromIso Zeit im ISO 8601 Format ("2025-07-10T08:00:00Z")
     */
    public void setFrom(String fromIso) {
        try {
            this.from = (fromIso == null || fromIso.isBlank()) ? null : Instant.parse(fromIso);
        } catch (DateTimeParseException e) {
            this.from = null;
        }
    }

    /**
     * Setzt den Endzeitpunkt für Zeitfilterung.
     * Bei ungültigem oder leerem Wert wird {@code null} gesetzt.
     *
     * @param toIso Zeit im ISO 8601 Format ("2025-07-10T12:00:00Z")
     */
    public void setTo(String toIso) {
        try {
            this.to = (toIso == null || toIso.isBlank()) ? null : Instant.parse(toIso);
        } catch (DateTimeParseException e) {
            this.to = null;
        }
    }

    /**
     * Setzt den Tag bzw. das Tag-Kriterium (Kategorie).
     *
     * @param tag Freitext-Tag, "Privat" oder "Feiertag"
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Setzt den Filter für Titel-Teilsuche.
     *
     * @param titleContains Teilwort, das im Titel enthalten sein soll
     */
    public void setTitleContains(String titleContains) {
        this.titleContains = titleContains;
    }

    /**
     * Setzt das Startdatum ("2025-08-01") für die Datumsbereichsfilterung.
     *
     * @param date Datum im Format yyyy-MM-dd
     */
    public void setDateFrom(String date) {
        this.dateFrom = date == null ? null : LocalDate.parse(date);
    }

    /**
     * Setzt das Enddatum ("2025-08-31") für die Datumsbereichsfilterung.
     *
     * @param date Datum im Format yyyy-MM-dd
     */
    public void setDateTo(String date) {
        this.dateTo = date == null ? null : LocalDate.parse(date);
    }

    // -------------------- Getter --------------------

    /**
     * @return CSV-String der gesetzten Wochentage
     */
    public String getWeekday() {
        return weekday;
    }

    /**
     * @return Liste von Monaten (1–12)
     */
    public List<Integer> getMonth() {
        return month;
    }

    /**
     * @return Startzeitpunkt als Instant
     */
    public Instant getFrom() {
        return from;
    }

    /**
     * @return Endzeitpunkt als Instant
     */
    public Instant getTo() {
        return to;
    }

    /**
     * @return Kategorie-Tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @return Teilwort für Titelfilter
     */
    public String getTitleContains() {
        return titleContains;
    }

    /**
     * @return Startdatum für Filterbereich
     */
    public LocalDate getDateFrom() {
        return dateFrom;
    }

    /**
     * @return Enddatum für Filterbereich
     */
    public LocalDate getDateTo() {
        return dateTo;
    }
}

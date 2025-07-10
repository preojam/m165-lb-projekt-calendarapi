package com.calendarapi.lbprojektm165calendarapi.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object (DTO) für Filterparameter zur Event-Suche.
 * Wandelt HTTP-Query-Parameter in geeignete Java-Typen um,
 * damit sie später in der Service-Schicht verwendet werden können.
 */
public class FilterDto {

    // Der rohe String vom Query-Param ("MONDAY,TUESDAY")
    private String weekday;

    // Liste von Monaten als Integer ([1, 2, 3])
    private List<Integer> month;

    // Zeitintervall: von wann bis wann ("2025-07-10T08:00:00Z")
    private Instant from;
    private Instant to;

    // Event-Tag ("Arbeit", "Privat", "Feiertag")
    private String tag;

    // Teilstring, der im Titel enthalten sein soll
    private String titleContains;

    // Optionaler Datumsbereich (Start und Ende)
    private LocalDate dateFrom;
    private LocalDate dateTo;

    /**
     * Setzt den Wochentag-Filter als CSV-String.
     * Wird später vermutlich im Service weiter verarbeitet.
     *
     * @param weekdaysCsv Kommagetrennte Liste von Tagen( "MONDAY,TUESDAY")
     */
    public void setWeekday(String weekdaysCsv) {
        this.weekday = weekdaysCsv; // keine Validierung hier
    }

    /**
     * Setzt die Monate aus einem CSV-String
     * Wandelt sie in eine Liste von Integern um.
     *
     * @param monthsCsv Kommagetrennte Monatszahlen
     */
    public void setMonth(String monthsCsv) {
        this.month = (monthsCsv == null || monthsCsv.isBlank())
                ? Collections.emptyList() // Wenn leer, dann leere Liste
                : List.of(monthsCsv.split(",")) // Aufteilen bei Kommas
                .stream()
                .map(Integer::parseInt) // Umwandeln in Integer
                .collect(Collectors.toList());
    }

    /**
     * Setzt den Startzeitpunkt (Zeitstempel im ISO 8601-Format).
     *
     * @param fromIso z. B. "2025-07-10T08:00:00Z"
     */
    public void setFrom(String fromIso) {
        try {
            this.from = (fromIso == null || fromIso.isBlank()) ? null : Instant.parse(fromIso);
        } catch (DateTimeParseException e) {
            this.from = null; // Falls Parsing fehlschlägt → ignorieren
        }
    }

    /**
     * Setzt den Endzeitpunkt (Zeitstempel im ISO 8601-Format).
     *
     * @param toIso z. B. "2025-07-10T12:00:00Z"
     */
    public void setTo(String toIso) {
        try {
            this.to = (toIso == null || toIso.isBlank()) ? null : Instant.parse(toIso);
        } catch (DateTimeParseException e) {
            this.to = null; // Fehlerhafte Eingabe wird ignoriert
        }
    }

    /**
     * Setzt den Tag (Kategorie-Tag des Events).
     *
     * @param tag Einfache Zeichenkette (z. B. "Arbeit")
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Setzt den Titel-Suchfilter.
     * @param titleContains Teilstring, der im Eventtitel vorkommen soll
     */
    public void setTitleContains(String titleContains) {
        this.titleContains = titleContains;
    }

    /**
     * Setzt das Feld dateFrom basierend auf einem ISO-8601-Datum (z. B. "2025-08-01").
     * Wandelt den String in ein LocalDate-Objekt um, das später zur Filterung verwendet werden kann.
     * Falls der Eingabewert null ist, wird auch das Feld auf null gesetzt.
     *
     * @param date das Datum im Format "yyyy-MM-dd" (z. B. "2025-08-01")
     */
    public void setDateFrom(String date) {
        this.dateFrom = date == null ? null : LocalDate.parse(date); // Konvertierung mit parse()
    }

    /**
     * Setzt das Feld dateTo basierend auf einem ISO-8601-Datum (z. B. "2025-08-31").
     * Wandelt den String in ein LocalDate-Objekt um.
     * Wenn kein Wert übergeben wird (null), wird auch das Attribut null.
     *
     * @param date das Datum im Format "yyyy-MM-dd"
     */
    public void setDateTo(String date) {
        this.dateTo = date == null ? null : LocalDate.parse(date); // ISO-Format erwartet
    }

    // ---------- Getter ----------

    public String getWeekday() {
        return weekday;
    }

    public List<Integer> getMonth() {
        return month;
    }

    public Instant getFrom() {
        return from;
    }

    public Instant getTo() {
        return to;
    }

    public String getTag() {
        return tag;
    }

    public String getTitleContains() {
        return titleContains;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }
}

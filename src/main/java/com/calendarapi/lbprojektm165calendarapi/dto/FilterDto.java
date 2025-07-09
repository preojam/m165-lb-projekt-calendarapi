package com.calendarapi.lbprojektm165calendarapi.dto;

import org.springframework.cglib.core.Local;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FilterDto {
    // Der rohe String vom Query-Param (z. B. "MONDAY,TUESDAY")
    private String weekday;

    private List<Integer> month;
    private Instant from;
    private Instant to;
    private String tag;
    private String titleContains;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    // Setter für weekday (CSV-String direkt speichern)
    public void setWeekday(String weekdaysCsv) {
        this.weekday = weekdaysCsv;
    }

    // Setter für Monate als Liste
    public void setMonth(String monthsCsv) {
        this.month = (monthsCsv == null || monthsCsv.isBlank())
                ? Collections.emptyList()
                : List.of(monthsCsv.split(","))
                      .stream()
                      .map(Integer::parseInt)
                      .collect(Collectors.toList());
    }

    public void setFrom(String fromIso) {
        try {
            this.from = (fromIso == null || fromIso.isBlank()) ? null : Instant.parse(fromIso);
        } catch (DateTimeParseException e) {
            this.from = null;
        }
    }

    public void setTo(String toIso) {
        try {
            this.to = (toIso == null || toIso.isBlank()) ? null : Instant.parse(toIso);
        } catch (DateTimeParseException e) {
            this.to = null;
        }
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

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
        this.dateFrom = date == null ? null : LocalDate.parse(date);
    }

    /**
     * Setzt das Feld dateTo basierend auf einem ISO-8601-Datum (z. B. "2025-08-31").
     * Wandelt den String in ein LocalDate-Objekt um.
     * Wenn kein Wert übergeben wird (null), wird auch das Attribut null.
     *
     * @param date das Datum im Format "yyyy-MM-dd"
     */
    public void setDateTo(String date) {
        this.dateTo = date == null ? null : LocalDate.parse(date);
    }

    // Getter
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

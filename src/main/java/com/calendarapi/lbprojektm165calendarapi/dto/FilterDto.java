package com.calendarapi.lbprojektm165calendarapi.dto;

import java.time.Instant;
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
}

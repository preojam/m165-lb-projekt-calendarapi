package com.calendarapi.lbprojektm165calendarapi.dto; //data transfer object

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FilterDto {
    private List<String> weekday;
    private List<Integer> month;
    private Instant from;
    private Instant to;
    private String tag;

    // Setzt Wochentage aus CSV-String wie "MONDAY,TUESDAY"
    public void setWeekday(String weekdaysCsv) {
        this.weekday = (weekdaysCsv == null || weekdaysCsv.isBlank())
                ? Collections.emptyList()
                : Arrays.asList(weekdaysCsv.toUpperCase().split(","));
    }

    // Setzt Monate aus CSV-String wie "1,5,12"
    public void setMonth(String monthsCsv) {
        this.month = (monthsCsv == null || monthsCsv.isBlank())
                ? Collections.emptyList()
                : Arrays.stream(monthsCsv.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public void setFrom(String fromIso) {
        try {
            this.from = fromIso == null ? null : Instant.parse(fromIso);
        } catch (DateTimeParseException e) {
            this.from = null;
        }
    }

    public void setTo(String toIso) {
        try {
            this.to = toIso == null ? null : Instant.parse(toIso);
        } catch (DateTimeParseException e) {
            this.to = null;
        }
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getWeekday() { return weekday; }
    public List<Integer> getMonth() { return month; }
    public Instant getFrom() { return from; }
    public Instant getTo() { return to; }
    public String getTag() { return tag; }
}
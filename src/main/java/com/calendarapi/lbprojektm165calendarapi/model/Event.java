package com.calendarapi.lbprojektm165calendarapi.model;

import com.fasterxml.jackson.core.JsonToken;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

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

    // Vollständiger Konstruktor
    public Event(String title, String description,
                 Instant start, Instant end,
                 String cron,
                 List<String> tags,
                 List<String> daysOfWeek,
                 Integer dayOfMonth,
                 List<Integer> months) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.cron = cron;
        this.tags = tags;
        this.daysOfWeek = daysOfWeek;
        this.dayOfMonth = dayOfMonth;
        this.months = months;
    }


}

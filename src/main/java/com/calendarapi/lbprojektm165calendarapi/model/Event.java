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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getMonths() {
        return months;
    }

    public void setMonths(List<Integer> months) {
        this.months = months;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}

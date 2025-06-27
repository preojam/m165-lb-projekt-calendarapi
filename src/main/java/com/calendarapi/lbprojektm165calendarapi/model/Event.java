package com.calendarapi.lbprojektm165calendarapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "events")
public class Event {

    @Id
    private String id;

    private String title;
    private String description;
    private Instant start;
    private Instant end;
    private String cron;               // für das Cron-Pattern
    private List<String> tags;         // für Tags
    private List<String> daysOfWeek;   // für Filter
    private Integer dayOfMonth;
    private List<Integer> months;

    // Leerer Konstruktor
    public Event() {}

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

    // Getter & Setter für id
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    // Getter & Setter für title
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter & Setter für description
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter & Setter für start
    public Instant getStart() {
        return start;
    }
    public void setStart(Instant start) {
        this.start = start;
    }

    // Getter & Setter für end
    public Instant getEnd() {
        return end;
    }
    public void setEnd(Instant end) {
        this.end = end;
    }

    // Getter & Setter für cron
    public String getCron() {
        return cron;
    }
    public void setCron(String cron) {
        this.cron = cron;
    }

    // Getter & Setter für tags
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    // Getter & Setter für daysOfWeek
    public List<String> getDaysOfWeek() {
        return daysOfWeek;
    }
    public void setDaysOfWeek(List<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    // Getter & Setter für dayOfMonth
    public Integer getDayOfMonth() {
        return dayOfMonth;
    }
    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    // Getter & Setter für months
    public List<Integer> getMonths() {
        return months;
    }
    public void setMonths(List<Integer> months) {
        this.months = months;
    }
}

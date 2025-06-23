package com.calendarapi.lbprojektm165calendarapi.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "events")
public class Event {

    @Id
    private String id;

    private String title;
    private Instant startTime;
    private Instant endTime;
    private String weekday; // z. B. MONDAY
    private Integer month;  // 1–12
    private String tag;     // z. B. "work", "private"

    // Konstruktoren
    public Event() {}

    public Event(String title, Instant startTime, Instant endTime, String weekday, Integer month, String tag) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weekday = weekday;
        this.month = month;
        this.tag = tag;
    }

    // Getter und Setter
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
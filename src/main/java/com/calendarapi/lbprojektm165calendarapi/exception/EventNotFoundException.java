package com.calendarapi.lbprojektm165calendarapi.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String id) {
        super("Kein Event mit der ID '" + id + "' gefunden.");
    }
}

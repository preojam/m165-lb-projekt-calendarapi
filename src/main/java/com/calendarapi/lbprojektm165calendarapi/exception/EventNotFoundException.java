package com.calendarapi.lbprojektm165calendarapi.exception;

import com.calendarapi.lbprojektm165calendarapi.model.Event;

/**
 * Wird geworfen, wenn kein {@code Event} mit der angegebenen ID gefunden werden kann.
 * <p>
 * Diese Ausnahme erweitert {@link RuntimeException} und kennzeichnet fehlgeschlagene
 * Suchvorgänge nach {@link Event}-Objekten in der Calendar-API.
 * </p>
 *
 * @author Preo
 * @version 1.0
 * @since 1.0
 * @see com.calendarapi.lbprojektm165calendarapi.model.Event
 */
public class EventNotFoundException extends RuntimeException {

    /**
     * Erzeugt eine neue {@code EventNotFoundException} mit einer
     * ausführlichen Fehlermeldung, die die ID des nicht gefundenen Events enthält.
     *
     * @param id die ID des Events, das nicht gefunden wurde
     */
    public EventNotFoundException(String id) {
        super("Kein Event mit der ID '" + id + "' gefunden.");
    }
}

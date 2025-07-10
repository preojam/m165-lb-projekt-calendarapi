package com.calendarapi.lbprojektm165calendarapi.exception;

/**
 * Wird geworfen, wenn kein {@code Event} mit der angegebenen ID gefunden werden kann.
 * <p>
 * Diese Ausnahme erweitert {@link RuntimeException} und dient dazu, fehlgeschlagene
 * Suchvorgänge nach Events in der Calendar-API eindeutig zu kennzeichnen.
 * </p>
 *
 * @since 1.0
 * @see com.calendarapi.lbprojektm165calendarapi.model.Event
 */
public class EventNotFoundException extends RuntimeException {

    /**
     * Erzeugt eine neue {@code EventNotFoundException} mit einer
     * aussagekräftigen Fehlermeldung, die die gesuchte ID enthält.
     *
     * @param id die ID des Events, das nicht gefunden wurde
     */
    public EventNotFoundException(String id) {
        super("Kein Event mit der ID '" + id + "' gefunden.");
    }
}

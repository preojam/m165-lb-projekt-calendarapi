package com.calendarapi.lbprojektm165calendarapi.exception;

/**
 * Wird geworfen, wenn ein ungültiger Cron-Ausdruck im QUARTZ-Format erkannt wird.
 * <p>
 * Diese Ausnahme erweitert {@link RuntimeException} und signalisiert,
 * dass die angegebene Cron-Expression nicht geparst oder ausgeführt werden kann.
 * </p>
 *
 * @author Preo
 * @version 1.0
 * @since 1.0
 */
public class InvalidCronException extends RuntimeException {

    /**
     * Erzeugt eine neue InvalidCronException mit einer
     * erläuternden Fehlermeldung.
     *
     * @param message die detailierte Fehlermeldung zum ungültigen Cron-Ausdruck
     */
    public InvalidCronException(String message) {
        super(message);
    }
}

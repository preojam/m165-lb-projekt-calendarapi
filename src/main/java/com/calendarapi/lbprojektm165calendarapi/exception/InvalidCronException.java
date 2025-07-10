package com.calendarapi.lbprojektm165calendarapi.exception;

/**
 * Wird geworfen, wenn ein ungültiger Cron-Ausdruck erkannt wird.
 */
public class InvalidCronException extends RuntimeException {

    /**
     * Erzeugt eine neue InvalidCronException mit einer
     * erläuternden Fehlermeldung.
     *
     * @param message die Fehlermeldung zum ungültigen Cron-Ausdruck
     */
    public InvalidCronException(String message) {
        super(message);
    }
}

package com.calendarapi.lbprojektm165calendarapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Zentrale Exception-Handler-Klasse für REST-Controller.
 * <p>
 * Fängt verschiedene Ausnahmen ab und wandelt sie in
 * geeignete HTTP-Antworten mit Statuscodes und Fehlermeldungen um.
 * </p>
 */
@ControllerAdvice
public class RestExceptionHandler {

    /**
     * Behandelt Validierungsfehler (z. B. bei @Valid in DTOs).
     *
     * @param ex die MethodArgumentNotValidException mit allen Validierungsfehlern
     * @return ResponseEntity mit einer Map von Feldnamen zu Fehlermeldungen und HTTP 400
     */
    // Validierungsfehler (z. B. bei @Valid in DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String msg = error.getDefaultMessage();
            errors.put(field, msg);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Behandelt fehlerhafte Argumente in Methodenaufrufen.
     *
     * @param ex die IllegalArgumentException mit einer Beschreibung des Fehlers
     * @return ResponseEntity mit der Fehlermeldung und HTTP 400
     */
    // Fehlerhafte Argumente
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArg(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Behandelt ungültige Cron-Ausdrücke.
     *
     * @param ex die InvalidCronException mit Details zum ungültigen Muster
     * @return ResponseEntity mit der Fehlermeldung und HTTP 400
     */
    // Ungültiges Cron-Pattern
    @ExceptionHandler(InvalidCronException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCron(InvalidCronException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Behandelt den Fall, dass ein Event nicht gefunden wird.
     *
     * @param ex die EventNotFoundException mit der gesuchten Event-ID
     * @return ResponseEntity mit der Fehlermeldung und HTTP 404
     */
    // Event wurde nicht gefunden
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEventNotFound(EventNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}

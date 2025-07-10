package com.calendarapi.lbprojektm165calendarapi.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse für die benutzerdefinierte Validierungsannotation {@link ValidCron}.
 * Diese Klasse testet, ob Cron-Ausdrücke gemäß Quartz-Syntax korrekt validiert werden.
 * <p>
 * Es werden folgende Szenarien abgedeckt:
 * <ul>
 *     <li>Gültiger Cron-Ausdruck (soll akzeptiert werden)</li>
 *     <li>Ungültiger Cron-Ausdruck (soll abgelehnt werden)</li>
 *     <li>Nullwert im Cron-Feld (soll abgelehnt werden)</li>
 * </ul>
 *
 * @author Arvin
 */
class ValidCronTest {

    /** Validator für die Bean-Validierung */
    private static Validator validator;

    /**
     * Initialisiert den Validator einmalig vor allen Tests.
     */
    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Wrapper-Klasse mit einem Feld, das mit {@link ValidCron} annotiert ist.
     * Wird für die Testfälle verwendet.
     */
    private static class TestCronWrapper {
        @ValidCron
        String cron;

        /**
         * Konstruktor zum Setzen des Cron-Ausdrucks.
         *
         * @param cron der zu prüfende Cron-Ausdruck
         */
        TestCronWrapper(String cron) {
            this.cron = cron;
        }
    }

    /**
     * Testet einen gültigen Cron-Ausdruck (Quartz-Format: täglich um 12:00 Uhr).
     * Erwartet keine Validierungsfehler.
     */
    @Test
    void validCronExpression_shouldPassValidation() {
        TestCronWrapper wrapper = new TestCronWrapper("0 0 12 * * ?");

        Set<ConstraintViolation<TestCronWrapper>> violations = validator.validate(wrapper);

        assertTrue(violations.isEmpty(), "Erwartet: keine Validierungsfehler für gültigen Cron");
    }

    /**
     * Testet einen ungültigen Cron-Ausdruck.
     * Erwartet mindestens einen Validierungsfehler.
     */
    @Test
    void invalidCronExpression_shouldFailValidation() {
        TestCronWrapper wrapper = new TestCronWrapper("INVALID CRON");

        Set<ConstraintViolation<TestCronWrapper>> violations = validator.validate(wrapper);

        assertFalse(violations.isEmpty(), "Erwartet: Validierungsfehler für ungültigen Cron");
    }

    /**
     * Testet einen null-Wert im Cron-Feld.
     * Erwartet mindestens einen Validierungsfehler.
     */
    @Test
    void nullCron_shouldFailValidation() {
        TestCronWrapper wrapper = new TestCronWrapper(null);

        Set<ConstraintViolation<TestCronWrapper>> violations = validator.validate(wrapper);

        assertFalse(violations.isEmpty(), "Erwartet: Validierungsfehler für null-Cron");
    }
}
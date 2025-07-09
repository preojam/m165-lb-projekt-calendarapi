package com.calendarapi.lbprojektm165calendarapi.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidCronTest {

    private static Validator validator;

    // Wird einmalig ausgeführt und initialisiert den Validator
    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Testklasse mit einem Cron-Feld, das @ValidCron verwendet
     */
    private static class TestCronWrapper {
        @ValidCron
        String cron;

        TestCronWrapper(String cron) {
            this.cron = cron;
        }
    }

    @Test
    void validCronExpression_shouldPassValidation() {
        // Ein gültiger Cron-Ausdruck im Quartz-Format (täglich um 12:00 Uhr)
        TestCronWrapper wrapper = new TestCronWrapper("0 0 12 * * ?");

        Set<ConstraintViolation<TestCronWrapper>> violations = validator.validate(wrapper);

        // Es dürfen keine Verstöße gefunden werden
        assertTrue(violations.isEmpty(), "Erwartet: keine Validierungsfehler für gültigen Cron");
    }

    @Test
    void invalidCronExpression_shouldFailValidation() {
        // Ein ungültiger Cron-Ausdruck (fehlende Felder, falsches Format)
        TestCronWrapper wrapper = new TestCronWrapper("INVALID CRON");

        Set<ConstraintViolation<TestCronWrapper>> violations = validator.validate(wrapper);

        // Es muss mindestens ein Fehler auftreten
        assertFalse(violations.isEmpty(), "Erwartet: Validierungsfehler für ungültigen Cron");
    }

    @Test
    void nullCron_shouldFailValidation() {
        // Nullwert → laut Validator nicht erlaubt
        TestCronWrapper wrapper = new TestCronWrapper(null);

        Set<ConstraintViolation<TestCronWrapper>> violations = validator.validate(wrapper);

        assertFalse(violations.isEmpty(), "Erwartet: Validierungsfehler für null-Cron");
    }
}
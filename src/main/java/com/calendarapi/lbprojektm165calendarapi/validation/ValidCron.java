package com.calendarapi.lbprojektm165calendarapi.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Diese Annotation wird verwendet, um ein Feld (String) als Cron-Ausdruck im QUARTZ-FORMAT zu validieren.
 * Der Cron-Ausdruck muss dem QUARTZ-Format entsprechen und folgende Regeln einhalten:
 * - Besteht aus 6 oder 7 Feldern: Sekunden, Minuten, Stunden, Tag des Monats, Monat, Tag der Woche, (Jahr)
 * - Beispiel: "0 0 12 * * ?" (Täglich um 12:00:00 Uhr)
 * - Unterstützt Sonderzeichen: * (jeder Wert), ? (kein spezifischer Wert),
 *   - (Bereich), , (Werteliste), / (Schritte), L (letzter), W (Werktag), # (n-ter Wochentag)
 *
 * Verwendungsbeispiel:
 * @ValidCron
 * private String cronExpression;
 */
@Documented
@Constraint(validatedBy = ValidCron.ValidCronValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCron {

    String message() default "Ungültiger Cron-Ausdruck im QUARTZ-Format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Validator-Implementierung für die @ValidCron Annotation.
     * Validiert, ob ein String ein gültiger QUARTZ Cron-Ausdruck ist.
     */
    class ValidCronValidator implements ConstraintValidator<ValidCron, String> {

        private static final Logger logger = LoggerFactory.getLogger(ValidCronValidator.class);

        private static final CronParser CRON_PARSER = new CronParser(
                CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ)
        );

        /**
         * Validiert einen Cron-Ausdruck.
         * @param value Der zu validierende Cron-Ausdruck
         * @param context Der Validierungskontext
         * @return true wenn der Ausdruck gültig ist, sonst false
         */
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            try {
                if (value == null || value.isBlank()) {
                    logger.debug("Cron-Ausdruck ist null oder leer");
                    return false;
                }

                CRON_PARSER.parse(value).validate();
                logger.debug("Cron-Ausdruck '{}' erfolgreich validiert", value);
                return true;

            } catch (IllegalArgumentException e) {
                logger.debug("Ungültiger Cron-Ausdruck '{}': {}", value, e.getMessage());
                return false;
            } catch (Exception e) {
                logger.warn("Unerwarteter Fehler bei der Validierung des Cron-Ausdrucks '{}': {}",
                        value, e.getMessage());
                return false;
            }
        }
    }
}
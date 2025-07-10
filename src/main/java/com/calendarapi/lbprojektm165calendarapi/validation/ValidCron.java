package com.calendarapi.lbprojektm165calendarapi.validation;

// Import von Annotationen und Validierungsinterfaces aus Jakarta Validation API
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

// Import von CronUtils-Klassen für Cron-Parsing und -Definition
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Eigene Annotation zur Validierung von Cron-Ausdrücken im Quartz-Format.
 * Diese Annotation kann an String-Feldern verwendet werden.
 */
@Documented // Annotation wird in der JavaDoc sichtbar gemacht
@Constraint(validatedBy = ValidCron.ValidCronValidator.class) // Verknüpft die Annotation mit dem Validator
@Target({ ElementType.FIELD }) // Annotation darf nur an Feldern verwendet werden
@Retention(RetentionPolicy.RUNTIME) // Annotation ist zur Laufzeit verfügbar
public @interface ValidCron {

    // Standard-Fehlermeldung, wenn der Cron-Ausdruck ungültig ist
    String message() default "Ungültiger Cron-Ausdruck im QUARTZ-Format";

    // Gruppenmechanismus für Validierungslogik (Standard leer)
    Class<?>[] groups() default {};

    // Nutzlast für erweiterte Informationen zur Validierung (Standard leer)
    Class<? extends Payload>[] payload() default {};

    /**
     * Innere Klasse, die die Validierungslogik für die @ValidCron Annotation implementiert.
     * Sie prüft, ob der übergebene String ein gültiger Cron-Ausdruck im QUARTZ-Format ist.
     */
    class ValidCronValidator implements ConstraintValidator<ValidCron, String> {

        // Logger für Debug- und Fehlermeldungen
        private static final Logger logger = LoggerFactory.getLogger(ValidCronValidator.class);

        // Parser für Cron-Ausdrücke nach dem QUARTZ-Standard
        private static final CronParser CRON_PARSER = new CronParser(
                CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ)
        );

        /**
         * Diese Methode führt die eigentliche Validierung durch.
         * @param value Der zu prüfende Cron-Ausdruck
         * @param context Kontextinformationen für die Validierung
         * @return true, wenn der Ausdruck gültig ist, sonst false
         */
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            try {
                // Wenn der Wert null oder leer ist, ist er ungültig
                if (value == null || value.isBlank()) {
                    logger.debug("Cron-Ausdruck ist null oder leer");
                    return false;
                }

                // Versuch, den Ausdruck zu parsen und zu validieren
                CRON_PARSER.parse(value).validate();
                logger.debug("Cron-Ausdruck '{}' erfolgreich validiert", value);
                return true;

                // Fängt ungültige Cron-Ausdrücke ab
            } catch (IllegalArgumentException e) {
                logger.debug("Ungültiger Cron-Ausdruck '{}': {}", value, e.getMessage());
                return false;

                // Fängt alle anderen unerwarteten Fehler ab
            } catch (Exception e) {
                logger.warn("Unerwarteter Fehler bei der Validierung des Cron-Ausdrucks '{}': {}",
                        value, e.getMessage());
                return false;
            }
        }
    }
}
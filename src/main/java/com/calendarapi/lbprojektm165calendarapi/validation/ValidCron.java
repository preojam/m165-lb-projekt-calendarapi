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
 * {@code @ValidCron} ist eine benutzerdefinierte Validierungsannotation zur Prüfung,
 * ob ein {@code String}-Feld einen gültigen Cron-Ausdruck im Quartz-Format enthält.
 * <p>
 * Diese Annotation kann auf Felder angewendet werden und verwendet intern CronUtils
 * zur strukturellen Validierung.
 *
 * <p><strong>Beispiel:</strong>
 * <pre>
 * {@code
 * @ValidCron
 * private String cronExpression;
 * }
 * </pre>
 *
 * @author Arvin
 */
@Documented
@Constraint(validatedBy = ValidCron.ValidCronValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCron {

    /**
     * Die Standardfehlermeldung, die zurückgegeben wird, wenn der Cron-Ausdruck ungültig ist.
     *
     * @return Fehlermeldung als {@code String}
     */
    String message() default "Ungültiger Cron-Ausdruck im QUARTZ-Format";

    /**
     * Gruppen für Validierungslogik, bei Gruppierung von Constraints.
     *
     * @return Klassenarray für Validierungsgruppen
     */
    Class<?>[] groups() default {};

    /**
     * Nutzlast für zusätzliche Metadaten innerhalb von Constraint-Definitionen.
     *
     * @return Array von Payload-Typen
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Validator-Klasse für {@link ValidCron}, implementiert die eigentliche Logik zur
     * Prüfung, ob der gegebene String ein valider Quartz-Cron-Ausdruck ist.
     */
    class ValidCronValidator implements ConstraintValidator<ValidCron, String> {

        /** Logger für Debugging und Fehlerdiagnose */
        private static final Logger logger = LoggerFactory.getLogger(ValidCronValidator.class);

        /** CronParser-Instanz für Quartz-Cron-Ausdrücke */
        private static final CronParser CRON_PARSER = new CronParser(
                CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ)
        );

        /**
         * Prüft, ob der übergebene String ein gültiger Cron-Ausdruck ist.
         *
         * @param value   der zu prüfende Cron-Ausdruck
         * @param context Kontext der Validierung (nicht verwendet)
         * @return {@code true}, wenn der Ausdruck gültig ist, sonst {@code false}
         */
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            try {
                if (value == null || value.isBlank()) {
                    logger.debug("Cron-Ausdruck ist null oder leer");
                    return false;
                }

                // Validiert den geparsten Cron-Ausdruck
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
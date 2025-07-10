package com.calendarapi.lbprojektm165calendarapi.init;

import com.calendarapi.lbprojektm165calendarapi.model.Event;
import com.calendarapi.lbprojektm165calendarapi.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.util.List;

/**
 * Initialisiert beim Start der Anwendung im "dev"-Profil Beispiel-Daten.
 * <p>
 * Löscht vorhandene Events und legt sechs vordefinierte Event-Objekte an.
 * Diese Daten dienen als Seed für die Calendar-API und erleichtern das Testen
 * und Entwickeln ohne manuelle Datenerstellung.
 * </p>
 *
 * @author Preo
 * @version 1.0
 * @since 1.0
 */
@Configuration
@Profile("dev")
public class DataInitializer {

    /**
     * Gibt einen {@link CommandLineRunner} zurück, der beim Anwendungsstart
     * im "dev"-Profil die Event-Datenbank zurücksetzt und mit Beispieldaten füllt.
     *
     * @param eventRepo das Repository, in dem die Beispiel-Events gespeichert werden
     * @return ein {@link CommandLineRunner}, der die Seed-Daten beim Start einfügt
     */
    @Bean
    @Profile("dev")
    public CommandLineRunner initData(EventRepository eventRepo) {
        return args -> {
            // Zuerst alle vorhandenen Events löschen
            eventRepo.deleteAll();

            // 1) Sprint Planning
            Event sprint = new Event();
            // Titel des Events: Name der Aktivität
            sprint.setTitle("Sprint Planning");
            // Beschreibung: Zweck und Inhalt des Meetings
            sprint.setDescription("Planung des nächsten Sprints");
            // Startzeitpunkt in UTC (ISO-Format) z = zulu time utc
            sprint.setStart(Instant.parse("2025-07-01T09:00:00Z"));
            // Endzeitpunkt in UTC
            sprint.setEnd(Instant.parse("2025-07-01T10:00:00Z"));
            // Cron-Expression: "0 0 9 ? * MON#1"
            // Führt das Event am ersten Montag jedes Monats um 09:00 Uhr aus
            sprint.setCron("0 0 9 ? * MON#1");
            // Tags: Kategorien zur Gruppierung des Events
            sprint.setTags(List.of("Agile", "Team"));
            // Wochentage: Hier MON für Montag
            sprint.setDaysOfWeek(List.of("MON"));
            // Tag des Monats: 1 (relevant für manche Cron-Patterns)
            sprint.setDayOfMonth(1);
            // Monate: 1–12 = Januar bis Dezember
            sprint.setMonths(List.of(1,2,3,4,5,6,7,8,9,10,11,12));

            // 2) Team Retro
            Event retro = new Event();
            retro.setTitle("Team Retro");
            retro.setDescription("Retrospektive zum letzten Sprint");
            retro.setStart(Instant.parse("2025-07-08T16:00:00Z"));
            retro.setEnd(Instant.parse("2025-07-08T17:00:00Z"));
            retro.setCron("0 0 16 ? * TUE#2");
            retro.setTags(List.of("Agile", "Review"));
            retro.setDaysOfWeek(List.of("TUE"));
            retro.setDayOfMonth(8);
            retro.setMonths(List.of(1,2,3,4,5,6,7,8,9,10,11,12));

            // 3) Marketing-Meeting
            Event marketing = new Event();
            marketing.setTitle("Marketing-Meeting");
            marketing.setDescription("Monatliche Kampagnen-Besprechung");
            marketing.setStart(Instant.parse("2025-07-15T14:00:00Z"));
            marketing.setEnd(Instant.parse("2025-07-15T15:30:00Z"));
            marketing.setCron("0 0 14 15 * ? *");
            marketing.setTags(List.of("Marketing", "Monatlich"));
            marketing.setDaysOfWeek(List.of());
            marketing.setDayOfMonth(15);
            marketing.setMonths(List.of(1,2,3,4,5,6,7,8,9,10,11,12));

            // 4) DevOps Check
            Event devops = new Event();
            devops.setTitle("DevOps Check");
            devops.setDescription("Wöchentlicher System-Health-Check");
            devops.setStart(Instant.parse("2025-07-01T03:00:00Z"));
            devops.setEnd(Instant.parse("2025-07-01T04:00:00Z"));
            devops.setCron("0 0 3 ? * SUN");
            devops.setTags(List.of("DevOps", "Monitoring"));
            devops.setDaysOfWeek(List.of("SUN"));
            devops.setDayOfMonth(null);
            devops.setMonths(List.of(1,2,3,4,5,6,7,8,9,10,11,12));

            // 5) Budget Review Q3
            Event budget = new Event();
            budget.setTitle("Budget Review Q3");
            budget.setDescription("Quartalsweise Finanz-Überprüfung");
            budget.setStart(Instant.parse("2025-07-01T11:00:00Z"));
            budget.setEnd(Instant.parse("2025-07-01T12:00:00Z"));
            budget.setCron("0 0 11 1 7,10,1 ?");
            budget.setTags(List.of("Finance", "Quarterly"));
            budget.setDaysOfWeek(List.of());
            budget.setDayOfMonth(1);
            budget.setMonths(List.of(1,4,7,10));

            // 6) Customer Webinar
            Event webinar = new Event();
            webinar.setTitle("Customer Webinar");
            webinar.setDescription("Vierteljährliches Online-Event für Kunden");
            webinar.setStart(Instant.parse("2025-08-20T17:00:00Z"));
            webinar.setEnd(Instant.parse("2025-08-20T18:00:00Z"));
            webinar.setCron("0 0 17 20 2,5,8,11 ?");
            webinar.setTags(List.of("Customer", "Webinar"));
            webinar.setDaysOfWeek(List.of());
            webinar.setDayOfMonth(20);
            webinar.setMonths(List.of(2,5,8,11));

            // Alle sechs Beispiel-Events speichern
            eventRepo.saveAll(List.of(
                    sprint,
                    retro,
                    marketing,
                    devops,
                    budget,
                    webinar
            ));

            System.out.println("Seeded Event collection with example Events.");
        };
    }
}

package com.calendarapi.lbprojektm165calendarapi.init;

import com.calendarapi.lbprojektm165calendarapi.model.Event;
import com.calendarapi.lbprojektm165calendarapi.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.util.List;

@Configuration
@Profile("dev")
public class DataInitializer {

    @Bean
    @Profile("dev")
    public CommandLineRunner initData(EventRepository eventRepo) {
        return args -> {
            // Zuerst alle vorhandenen Events löschen
            eventRepo.deleteAll();

            // 1) Sprint Planning
            Event sprint = new Event();
            sprint.setTitle("Sprint Planning");
            sprint.setDescription("Planung des nächsten Sprints");
            sprint.setStart(Instant.parse("2025-07-01T09:00:00Z"));
            sprint.setEnd(Instant.parse("2025-07-01T10:00:00Z"));
            sprint.setCron("0 0 9 ? * MON#1");
            sprint.setTags(List.of("Agile", "Team"));
            sprint.setDaysOfWeek(List.of("MON"));
            sprint.setDayOfMonth(1);
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

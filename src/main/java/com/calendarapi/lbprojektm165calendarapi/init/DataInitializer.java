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
public class DataInitializer {

    @Bean
    CommandLineRunner initData(EventRepository eventRepo) {
        System.out.println("**********************************************************");
        return args -> {
            eventRepo.deleteAll();

            // Erstes Beispiel-Event: Sprint Planning
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

            // Zweites Beispiel-Event: Team Retro
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

            // Speichern der Beispiel-Daten
            eventRepo.saveAll(List.of(sprint, retro));
            System.out.println("Seeded Event collection with example data.");


        };

    }
}

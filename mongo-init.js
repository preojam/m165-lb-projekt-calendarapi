// mongo-init.js

// Wechselt zur DB (wird automatisch angelegt)
db = db.getSiblingDB('calendar_db');

// Collection anlegen, falls sie nicht existiert
if (!db.getCollectionNames().includes('events')) {
    db.createCollection('events');
}

// Nur einmal befüllen, wenn leer
if (db.events.count() === 0) {
    db.events.insertMany([
        {
            // 1. Titel des Events
            title:       "Sprint Planning",           // String: Name des Termins
            // 2. Beschreibung des Events
            description: "Planung des nächsten Sprints",  // String: Details zum Zweck des Termins
            // 3. Startzeitpunkt (ISO-8601, UTC) (z = zulu time = UTC
            start:       ISODate("2025-07-01T09:00:00Z"),  // Datum und Uhrzeit, wann der Termin beginnt
            // 4. Endzeitpunkt (ISO-8601, UTC)
            end:         ISODate("2025-07-01T10:00:00Z"),  // Datum und Uhrzeit, wann der Termin endet
            // 5. Cron-Pattern (Quartz-Syntax)
            //    0 0 9 ? * MON#1
            //    └─ Sekunde:    0
            //       └─ Minute:  0
            //          └─ Stunde:   9
            //             └─ Tag des Monats: ? (nicht festgelegt)
            //                └─ Monat:        * (jeder Monat)
            //                   └─ Wochentag:   MON#1 (erster Montag im Monat)
            cron:        "0 0 9 ? * MON#1",              // String: Wiederholungsregel
            // 6. Schlagworte zur Kategorisierung
            tags:        ["Agile","Team"],               // Array<String>: z.B. zur Filterung in der UI
            // 7. Wochentage als Filter
            daysOfWeek:  ["MON"],                        // Array<String>: an welchen Wochentagen das Event gelten soll
            // 8. Tag im Monat
            dayOfMonth:  1,                              // Integer: fixer Kalendertag (1–31), falls gewünscht
            // 9. Monate als Filter
            months:      [1,2,3,4,5,6,7,8,9,10,11,12]    // Array<Integer>: welche Monate das Event einschließt
        },
        {
            title:       "Team Retro",
            description: "Retrospektive zum letzten Sprint",
            start:       ISODate("2025-07-08T16:00:00Z"),
            end:         ISODate("2025-07-08T17:00:00Z"),
            cron:        "0 0 16 ? * TUE#2",
            tags:        ["Agile","Review"],
            daysOfWeek:  ["TUE"],
            dayOfMonth:  8,
            months:      [1,2,3,4,5,6,7,8,9,10,11,12]
        },
        {
            title:       "Marketing-Meeting",
            description: "Monatliche Kampagnen-Besprechung",
            start:       ISODate("2025-07-15T14:00:00Z"),
            end:         ISODate("2025-07-15T15:30:00Z"),
            cron:        "0 0 14 15 * ? *",
            tags:        ["Marketing","Monatlich"],
            daysOfWeek:  [],              // TagOfWeek nicht relevant
            dayOfMonth:  15,
            months:      [1,2,3,4,5,6,7,8,9,10,11,12]
        },
        {
            title:       "DevOps Check",
            description: "Wöchentlicher System-Health-Check",
            start:       ISODate("2025-07-01T03:00:00Z"),
            end:         ISODate("2025-07-01T04:00:00Z"),
            cron:        "0 0 3 ? * SUN",
            tags:        ["DevOps","Monitoring"],
            daysOfWeek:  ["SUN"],
            dayOfMonth:  null,
            months:      [1,2,3,4,5,6,7,8,9,10,11,12]
        },
        {
            title:       "Budget Review Q3",
            description: "Quartalsweise Finanz-Überprüfung",
            start:       ISODate("2025-07-01T11:00:00Z"),
            end:         ISODate("2025-07-01T12:00:00Z"),
            cron:        "0 0 11 1 7,10,1 ?",
            tags:        ["Finance","Quarterly"],
            daysOfWeek:  [],
            dayOfMonth:  1,
            months:      [1,4,7,10]
        },
        {
            title:       "Customer Webinar",
            description: "Vierteljährliches Online-Event für Kunden",
            start:       ISODate("2025-08-20T17:00:00Z"),
            end:         ISODate("2025-08-20T18:00:00Z"),
            cron:        "0 0 17 20 2,5,8,11 ?",
            tags:        ["Customer","Webinar"],
            daysOfWeek:  [],
            dayOfMonth:  20,
            months:      [2,5,8,11]
        }
    ]);
    print("Initialized 'events' collection with 6 example documents.");
}

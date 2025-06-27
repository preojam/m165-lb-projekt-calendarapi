// mongo-init.js

// Wechselt zur Datenbank (wird automatisch angelegt)
db = db.getSiblingDB('calendar_db');

// Collection anlegen, falls sie nicht existiert
if (!db.getCollectionNames().includes('events')) {
    db.createCollection('events');
}

// Beispiel-Daten nur einmal einfügen, wenn Collection noch leer ist
if (db.events.countDocuments() === 0) {
    db.events.insertMany([
        {
            title:       "Sprint Planning",
            description: "Planung des nächsten Sprints",
            start:       ISODate("2025-07-01T09:00:00Z"),
            end:         ISODate("2025-07-01T10:00:00Z"),
            cron:        "0 0 9 ? * MON#1",
            tags:        ["Agile","Team"],
            daysOfWeek:  ["MON"],
            dayOfMonth:  1,
            months:      [1,2,3,4,5,6,7,8,9,10,11,12]
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
        }
    ]);
    print("Initialized 'events' collection with example documents.");
}

# 📅 Calendar API

Dieses Repository enthält eine Spring Boot Anwendung zur Verwaltung von Kalender-Events in einer MongoDB-Datenbank.

---

## 🔧 Features

- CRUD-Operationen für Events (Create, Read, Update, Delete)
- Flexible Wiederholung von Events per Quartz-Cron-Pattern
- Vollständige Filterung: Datum, Uhrzeit, Wochentag, Monat, Titel, Tag (Kategorie)
- Unterstützung für Cron-Validierung (QUARTZ-kompatibel)
- REST-Endpunkte: Einzel-, Batch-Erstellung und -Filterung
- Replica-Set-fähige MongoDB (Docker)
- Backup/Restore via Bash
- Testbar mit Insomnia-Collection
- Vollständige JavaDoc-Dokumentation

---

## 📁 Projektstruktur

<img width="464" height="588" alt="image" src="https://github.com/user-attachments/assets/13d8de66-442d-442a-ad94-6474ef8869ea" />


---

## 🚀 Endpunkte

| Methode | Pfad                   | Beschreibung                         |
|--------:|------------------------|--------------------------------------|
| `POST`  | `/api/events`          | Neues Event erstellen                |
| `GET`   | `/api/events`          | Alle Events listen (mit Filter)      |
| `GET`   | `/api/events/{id}`     | Einzelnes Event abrufen              |
| `PUT`   | `/api/events/{id}`     | Event aktualisieren                  |
| `DELETE`| `/api/events/{id}`     | Event löschen                        |
| `POST`  | `/api/events/batch`    | Mehrere Events gleichzeitig erstellen|
| `GET`   | `/api/events/hello`    | Test-Endpunkt („Hello World“)        |

---

## 🔍 Filterparameter (`GET /api/events`)

**Alle Parameter sind optional und kombinierbar:**

| Parameter        | Beschreibung                                             | Beispiel                          |
|------------------|----------------------------------------------------------|-----------------------------------|
| `from`           | Startzeitpunkt (ISO-Format)                              | `2025-07-01T08:00:00Z`            |
| `to`             | Endzeitpunkt (ISO-Format)                                | `2025-07-01T20:00:00Z`            |
| `dateFrom`       | Startdatum (`yyyy-MM-dd`)                                | `2025-08-01`                      |
| `dateTo`         | Enddatum (`yyyy-MM-dd`)                                  | `2025-08-31`                      |
| `weekday`        | Wochentage (englisch, CSV)                               | `MONDAY,TUESDAY`                 |
| `month`          | Monatsnummern (CSV)                                      | `1,3,12`                          |
| `tag`            | Kategorie-Tag (z. B. Arbeit, Privat, Feiertag)           | `Arbeit`                          |
| `titleContains`  | Teilwort im Titel (case-insensitive)                     | `Daily`                           |

### Beispiel:
GET /api/events?from=2025-07-01T08:00:00Z&to=2025-07-31T18:00:00Z&weekday=MONDAY&tag=Agile&titleContains=Daily

---

## 🧮 Internes MongoDB-Query-Mapping

| Filter-Feld     | MongoDB-Feld   | Operator         | Beispiel                                             |
|------------------|----------------|------------------|------------------------------------------------------|
| `weekday`        | `daysOfWeek`   | `$in`            | `{ daysOfWeek: { $in: ["MONDAY"] } }`               |
| `month`          | `months`       | `$in`            | `{ months: { $in: [1, 3, 5] } }`                    |
| `from`, `to`     | `start`        | `$gte`, `$lte`   | `{ start: { $gte: ISODate(...), $lte: ISODate(...) } }` |
| `tag`            | `tags`         | `$eq`            | `{ tags: "Feiertag" }`                              |
| `titleContains`  | `title`        | `$regex`, `i`    | `{ title: { $regex: ".*Sprint.*", $options: "i" } }`|
| `dateFrom`       | `start`        | `$gte`           | `{ start: { $gte: ISODate("2025-08-01T00:00:00Z") } }` |
| `dateTo`         | `end`          | `$lte` (+1 Tag)  | `{ end: { $lte: ISODate("2025-08-31T23:59:59Z") } }` |

---

### MongoDB-Filterbefehle
 1. Einloggen in die Shell (wenn du per Docker-Port verbindest):
mongosh "mongodb://localhost:27018/calendar_db" ODER docker exec -it calendarapi-mongo mongosh     
 2. Auf die richtige Datenbank wechseln
use calendar_db
 3a. ALLE Events löschen (Collection bleibt leer, Indexe bleiben erhalten)
db.events.deleteMany({})
 3b. Nur Events vor einem bestimmten Datum löschen, z.B. alle, die vor dem 1. Juli 2025 enden:
db.events.deleteMany({ end: { $lt: ISODate("2025-07-01T00:00:00Z") } })
 3c. Einzelne Events nach Titel filtern und löschen:
db.events.deleteOne({ title: "Sprint Planning" })
 4. Wenn du die gesamte Collection komplett entfernen willst (inkl. Indexen):
db.events.drop()
 5. Zur Kontrolle die Anzahl der verbliebenen Dokumente abfragen:
db.events.countDocuments({})    oder db.events.count()
 Suche nach Events mit dem Wochentag "MONDAY"

db.events.find({
  daysOfWeek: { $in: ["MONDAY"] }
})

## Suche nach Events in den Monaten Januar, März und Mai
db.events.find({
  months: { $in: [1, 3, 5] }
})

## Events zwischen zwei Zeitpunkten (Startzeit)
db.events.find({
  start: {
    $gte: ISODate("2025-07-01T08:00:00Z"),
    $lte: ISODate("2025-07-31T18:00:00Z")
  }
})

## Suche nach Events mit dem Tag "Feiertag"
db.events.find({
  tags: "Feiertag"
})

## Titel enthält das Wort "Sprint", case-insensitive
db.events.find({
  title: {
    $regex: ".*Sprint.*",
    $options: "i"
  }
})

## Startdatum ab dem 1. August 2025
db.events.find({
  start: {
    $gte: ISODate("2025-08-01T00:00:00Z")
  }
})

## Enddatum bis einschließlich 31. August 2025
db.events.find({
  end: {
    $lte: ISODate("2025-08-31T23:59:59Z")
  }
})

##kombinierte Befehle
db.events.find({
  daysOfWeek: { $in: ["MONDAY"] },
  tags: "Arbeit",
  start: { $gte: ISODate("2025-08-01T00:00:00Z") },
  end: { $lte: ISODate("2025-08-31T23:59:59Z") }
})

db.events.find({
  title: /Review/i
})
 
db.events.find({
  start: { $gte: ISODate("2025-08-01T00:00:00Z") }
})
 
db.events.find({
  start: {
    $gte: ISODate("2025-07-01T00:00:00Z"),
    $lt: ISODate("2025-08-01T00:00:00Z")
  }

})
db.events.find({ 
  daysOfWeek: "MON"
})
 

## 🧪 Tests

- **Unit-Tests** mit `JUnit5`, `Mockito`
- **Integrationstests** mit `MockMvc`
- **Insomnia-Test-Collection** im Ordner `/insomnia`

---

## 📦 Cron-Unterstützung

- Cron-Ausdrücke werden nach dem **QUARTZ-Standard (7 Felder)** geprüft.
- Validierung über `cron-utils` + eigene `ValidCron` Annotation
- Fehlerhafte Cron-Patterns führen zu `400 Bad Request`

---

## 💾 Backup & Restore

**Backup (`backup.sh`)**
```bash
mongodump --uri "$SPRING_DATA_MONGODB_URI" --archive=backup.gz --gzip
Restore (restore.sh)

mongorestore --uri "$SPRING_DATA_MONGODB_URI" --archive=backup.gz --gzip --drop
🐳 MongoDB Replica Set (Docker Compose)
docker-compose.yml

version: '3.8'
services:
  mongo1:
    image: mongo:5
    command: ["--replSet", "rs0"]
    ports: ["27017:27017"]
  mongo2:
    image: mongo:5
    command: ["--replSet", "rs0"]
    ports: ["27018:27017"]
  mongo3:
    image: mongo:5
    command: ["--replSet", "rs0"]
    ports: ["27019:27017"]
Initialisierung des Replica Sets

mongo --host localhost:27017 --eval '
  rs.initiate({
    _id: "rs0",
    members: [
      { _id: 0, host: "mongo1:27017" },
      { _id: 1, host: "mongo2:27017" },
      { _id: 2, host: "mongo3:27017" }
    ]
  })
'
📚 JavaDoc generieren
mvn javadoc:javadoc
Danach findest du die Dokumentation unter:
target/site/apidocs/index.html

👥 Team
Name	Verantwortungsbereich
Chris	Filterlogik, DTOs, JavaDoc, README
Rici	MongoDB-Queries, Repository-Implementierung
Arvin	Unit-Tests, Docker Setup
Preo	Projektstruktur, Controller, API-Design

© 2025 CalendarAPI Team – Modul M165, GBC Schule für Informatik

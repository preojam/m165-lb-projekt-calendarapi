# Calendar API

Dieses Repository enthält eine Spring Boot Anwendung zur Verwaltung von Kalender-Events in einer MongoDB-Datenbank.

## Features

* CRUD-Operationen für Events (Create, Read, Update, Delete)
* Flexible Wiederholung von Events per Cron-Pattern (Quartz-Format)
* Filterung nach Datum, Zeit, Wochentag, Monat und Tags
* Single-User-System (keine Authentifizierung)
* Backup/Restore-Skripte und Replica-Set-Konfiguration
* Insomnia-Collection für API-Tests

## Getting Started

### 1. Lokales Projekt einrichten

1. Neuen Ordner anlegen und hineinwechseln:

   ```powershell
   mkdir LB-Projekt-M165_Kalender
   cd LB-Projekt-M165_Kalender
   ```
2. Git-Repository initialisieren:

   ```powershell
   git init
   ```
3. Erste Datei erstellen (z.B. README.md):

   ```powershell
   New-Item README.md -Value "# LB-Projekt-M165_Kalender`nErste Inhalte..."
   ```
4. Git-Konfiguration prüfen:

   ```powershell
   git config user.name    # sollte Deinen GitHub-Nutzernamen zeigen
   git config user.email   # sollte Deine GitHub-E-Mail zeigen
   ```

### 2. Dateien committen & Branch umbenennen

1. Dateien zur Staging-Area hinzufügen:

   ```powershell
   git add .
   ```
2. Ersten Commit erstellen:

   ```powershell
   git commit -m "Initial commit mit README und Projektstruktur"
   ```
3. Branch zu `main` umbenennen (GitHub-Standard):

   ```powershell
   git branch -M main
   ```

### 3. Remote-Repository konfigurieren & Push

1. GitHub-Repository anlegen über GitHub-Webinterface (ohne README):

   * Name: `LB-Projekt-M165_Kalender`
   * **nicht** mit README initialisieren
2. Remote-URL hinzufügen:

   ```powershell
   git remote add origin https://github.com/<USERNAME>/LB-Projekt-M165_Kalender.git
   ```
3. Ersten Push durchführen und Upstream setzen:

   ```powershell
   git push -u origin main
   ```

> **Hinweis:** Um lokal `master` beizubehalten, kannst Du alternativ pushen mit:
>
> ```powershell
> git push -u origin master:main
> ```

## .gitignore

Lege eine `.gitignore` im Projektroot an für Java/Maven/IDE-Dateien:

```
# Java
*.class
*.jar
*.war

# Maven
/target/

# IntelliJ IDEA
/.idea/
*.iml

# VS Code
.vscode/

# Logs
logs/
*.log
```

## Projektstruktur

```
├── src
│   ├── main
│   │   ├── java/com/example/calendarapi
│   │   │   ├── CalendarApiApplication.java
│   │   │   ├── model/Event.java
│   │   │   ├── repository/EventRepository.java
│   │   │   └── controller/EventController.java
│   └── test
│       └── java/... (Tests)
├── docker-compose.yml
├── backup.sh
├── restore.sh
├── insomnia/
├── README.md
└── .gitignore
```

## Endpoints

| Methode | Pfad           | Beschreibung                             |
| ------- | -------------- | ---------------------------------------- |
| POST    | `/events`      | neues Event anlegen                      |
| GET     | `/events`      | alle Events listen + Filtermöglichkeiten |
| GET     | `/events/{id}` | einzelnes Event abrufen                  |
| PUT     | `/events/{id}` | Event aktualisieren                      |
| DELETE  | `/events/{id}` | Event löschen                            |

### Filter-Parameter

* `from` (ISO-Datum): Start des Zeitraums
* `to` (ISO-Datum): Ende des Zeitraums
* `weekday`: Wochentag(e), z.B. `MON,TUE`
* `month`: Monat(e), z.B. `7,8`
* `tag`: Event-Tag, z.B. `Meeting`

Beispiel:

```
GET /events?from=2025-07-01&to=2025-07-31&weekday=MON&tag=Agile
```

## Backup & Restore

Skript `backup.sh`:

```bash
#!/bin/bash
mongodump --uri "$SPRING_DATA_MONGODB_URI" --archive=backup.gz --gzip
```

Skript `restore.sh`:

```bash
#!/bin/bash
mongorestore --uri "$SPRING_DATA_MONGODB_URI" --archive=backup.gz --gzip --drop
```

## Replica Set (Docker-Compose)

```yaml
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
```

Init Replica Set:

```bash
mongo --host localhost:27017 --eval 'rs.initiate({_id: "rs0", members: [{_id:0, host: "mongo1:27017"}, {_id:1, host: "mongo2:27017"}, {_id:2, host: "mongo3:27017"}]})'
```

## Tests

* Insomnia-Collection im Ordner `insomnia/`
* Unit- & Integration-Tests mit JUnit & MockMvc

---
## Team: Preo, Arvin, Chris, Rici
© 2025 CalendarAPI Team

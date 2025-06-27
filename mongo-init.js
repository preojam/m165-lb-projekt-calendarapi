// mongo-init.js

// Wechselt zur Datenbank (wird automatisch angelegt)
db = db.getSiblingDB('calendar_db');

// Collection anlegen, falls sie nicht existiert
if (!db.getCollectionNames().includes('events')) {
    db.createCollection('events');
}

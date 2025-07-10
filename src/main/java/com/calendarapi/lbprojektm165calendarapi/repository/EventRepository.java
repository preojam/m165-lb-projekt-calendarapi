package com.calendarapi.lbprojektm165calendarapi.repository;

import com.calendarapi.lbprojektm165calendarapi.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository-Interface für {@link Event}-Entitäten.
 *
 * <p>Ermöglicht CRUD-Operationen sowie benutzerdefinierte Abfragen über {@link EventRepositoryCustom}.</p>
 *
 * <p>Wird automatisch von Spring Data implementiert. Standardmethoden wie {@code findAll()}, {@code save()}, {@code deleteById()} usw.
 * sind verfügbar. Zusätzlich können eigene Methoden über {@link EventRepositoryCustom} hinzugefügt werden.</p>
 *
 * @author Ricardo
 */
public interface EventRepository extends MongoRepository<Event, String>, EventRepositoryCustom {

    // Keine zusätzlichen Methoden hier notwendig – Standardmethoden werden von MongoRepository bereitgestellt.
    // Erweiterungen befinden sich in EventRepositoryCustom und dessen Implementierung.
}

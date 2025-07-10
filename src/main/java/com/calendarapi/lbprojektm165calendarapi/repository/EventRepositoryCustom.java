package com.calendarapi.lbprojektm165calendarapi.repository;

import com.calendarapi.lbprojektm165calendarapi.dto.FilterDto;
import com.calendarapi.lbprojektm165calendarapi.model.Event;

import java.time.Instant;
import java.util.List;

/**
 * Benutzerdefinierte Repository-Schnittstelle für komplexe Event-Abfragen.
 * Diese wird verwendet, wenn Abfragen über einfache Methoden wie findById() hinausgehen.
 *
 * Wird typischerweise in einer eigenen Implementierungsklasse umgesetzt, z. B. EventRepositoryImpl.
 */
public interface EventRepositoryCustom {

    /**
     * Findet Events basierend auf verschiedenen Filterkriterien.
     * Die Filter werden in einem {@link FilterDto} übergeben.
     *
     * @param filter Ein Objekt mit den gewünschten Filterkriterien (Wochentag, Monat, Zeit, Titel usw.)
     * @return Liste von Events, die den Kriterien entsprechen
     */
    List<Event> findByFilters(FilterDto filter); // Methode zur dynamischen Abfrage auf Basis von Filterwerten
}

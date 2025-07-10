package com.calendarapi.lbprojektm165calendarapi.repository;

import com.calendarapi.lbprojektm165calendarapi.dto.FilterDto;
import com.calendarapi.lbprojektm165calendarapi.model.Event;

import java.util.List;

/**
 * Benutzerdefinierte Repository-Schnittstelle für komplexe Event-Abfragen.
 *
 * <p>Diese Schnittstelle wird verwendet, wenn die Anforderungen über einfache CRUD-Methoden
 * (wie {@code findById()}, {@code findAll()} usw.) hinausgehen, z.B. bei dynamischen Filtern.</p>
 *
 * Die tatsächliche Implementierung erfolgt in {@code EventRepositoryImpl}.
 *
 * Beispiele für Filter:
 * <ul>
 *     <li>Wochentage (z.B. "Monday", "Tuesday")</li>
 *     <li>Monate (z.B. "January", "February")</li>
 *     <li>Datumsspanne (Start- und Endzeitpunkt)</li>
 *     <li>Tags (z.B. "work", "birthday")</li>
 *     <li>Titel-Suchbegriff (case-insensitive)</li>
 * </ul>
 *
 * @author Ricardo Cardoso
 */
public interface EventRepositoryCustom {

    /**
     * Findet {@link Event}-Objekte basierend auf den übergebenen Filterkriterien.
     *
     * @param filter ein {@link FilterDto}-Objekt mit allen gewünschten Filterparametern
     * @return Liste von {@link Event}-Objekten, die den angegebenen Kriterien entsprechen
     */
    List<Event> findByFilters(FilterDto filter);
}

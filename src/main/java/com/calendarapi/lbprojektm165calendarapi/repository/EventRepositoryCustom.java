package com.calendarapi.lbprojektm165calendarapi.repository;

import com.calendarapi.lbprojektm165calendarapi.model.Event;

import java.time.Instant;
import java.util.List;

public interface EventRepositoryCustom {
    List<Event> findByFilters(List<String> weekdays,
                              List<Integer> months,
                              Instant from,
                              Instant to,
                              String tag);
}
package com.calendarapi.lbprojektm165calendarapi.repository;

import com.calendarapi.lbprojektm165calendarapi.dto.FilterDto;
import com.calendarapi.lbprojektm165calendarapi.model.Event;

import java.time.Instant;
import java.util.List;

public interface EventRepositoryCustom {
    List<Event> findByFilters(FilterDto filter);
}
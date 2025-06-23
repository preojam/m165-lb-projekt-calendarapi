package com.calendarapi.lbprojektm165calendarapi.service;

import com.calendarapi.lbprojektm165calendarapi.dto.FilterDto;
import com.calendarapi.lbprojektm165calendarapi.model.Event;
import com.calendarapi.lbprojektm165calendarapi.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public List<Event> listEvents(FilterDto filter) {
        return repository.findByFilters(
                filter.getWeekday(),
                filter.getMonth(),
                filter.getFrom(),
                filter.getTo(),
                filter.getTag()
        );
    }
}
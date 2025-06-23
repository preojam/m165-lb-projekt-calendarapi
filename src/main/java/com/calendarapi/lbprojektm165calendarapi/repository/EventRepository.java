package com.calendarapi.lbprojektm165calendarapi.repository;


import com.calendarapi.lbprojektm165calendarapi.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String>, EventRepositoryCustom {
}
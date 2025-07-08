package com.calendarapi.lbprojektm165calendarapi.repository;

import com.calendarapi.lbprojektm165calendarapi.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.Instant;
import java.util.List;


public class EventRepositoryImpl implements EventRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Event> findByFilters(List<String> weekdays, List<Integer> months, Instant from, Instant to, String tag) {
        Query query = new Query();

        if (weekdays != null && !weekdays.isEmpty()) {
            query.addCriteria(Criteria.where("weekday").in(weekdays));
        }

        if (months != null && !months.isEmpty()) {
            query.addCriteria(Criteria.where("month").in(months));
        }

        if (from != null && to != null) {
            query.addCriteria(Criteria.where("startTime").gte(from).lte(to));
        } else if (from != null) {
            query.addCriteria(Criteria.where("startTime").gte(from));
        } else if (to != null) {
            query.addCriteria(Criteria.where("startTime").lte(to));
        }

        if (tag != null && !tag.isBlank()) {
            query.addCriteria(Criteria.where("tag").is(tag));
        }

        return mongoTemplate.find(query, Event.class);
    }
}
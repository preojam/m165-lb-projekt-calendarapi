package com.calendarapi.lbprojektm165calendarapi.repository;

import com.calendarapi.lbprojektm165calendarapi.dto.FilterDto;
import com.calendarapi.lbprojektm165calendarapi.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.Instant;
import java.util.List;
import java.util.regex.Pattern;


public class EventRepositoryImpl implements EventRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Event> findByFilters(FilterDto filter) {
        Query query = new Query();

        if (filter.getWeekday() != null && !filter.getWeekday().isEmpty()) {
            query.addCriteria(Criteria.where("weekday").in(filter.getWeekday()));
        }

        if (filter.getMonth() != null && !filter.getMonth().isEmpty()) {
            query.addCriteria(Criteria.where("month").in(filter.getMonth()));
        }

        if (filter.getFrom() != null && filter.getTo() != null) {
            query.addCriteria(Criteria.where("startTime").gte(filter.getFrom()).lte(filter.getTo()));
        } else if (filter.getFrom() != null) {
            query.addCriteria(Criteria.where("startTime").gte(filter.getFrom()));
        } else if (filter.getTo() != null) {
            query.addCriteria(Criteria.where("startTime").lte(filter.getTo()));
        }

        if (filter.getTag() != null && !filter.getTag().isBlank()) {
            query.addCriteria(Criteria.where("tag").is(filter.getTag()));
        }

        if (filter.getTitleContains() != null) {
            query.addCriteria(Criteria.where("title").regex(".*" + Pattern.quote(filter.getTitleContains()) + ".*", "i"));
        }

        if (filter.getTitleContains() != null && !filter.getTitleContains().isBlank()) {
            query.addCriteria(Criteria.where("title")
                    .regex(".*" + Pattern.quote(filter.getTitleContains()) + ".*", "i"));
        }

        return mongoTemplate.find(query, Event.class);
    }
}
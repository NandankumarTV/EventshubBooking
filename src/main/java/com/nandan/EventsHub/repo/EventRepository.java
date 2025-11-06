package com.nandan.EventsHub.repo;

import com.nandan.EventsHub.model.Event;
import com.nandan.EventsHub.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByEventTypeIgnoreCaseAndLocationIgnoreCase(String eventType, String location);

    Optional<Event> findByEventName(String eventName);

    List<Event> findByEventTypeIgnoreCase(String eventType);

    boolean existsByEventNameIgnoreCase(String eventName);

    List<Event> findByEventNameContainingIgnoreCase(String eventName);

    List<Event> findByEventType(String type);

    Optional<Event> findById(Long eventId);
}

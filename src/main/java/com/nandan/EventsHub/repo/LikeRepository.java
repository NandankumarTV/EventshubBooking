package com.nandan.EventsHub.repo;

import com.nandan.EventsHub.model.Event;
import com.nandan.EventsHub.model.Like;
import com.nandan.EventsHub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    // ✅ Check if a user already liked the event
    boolean existsByEventAndUser(Event event, User user);

    // ✅ Count total likes for an event
    long countByEvent(Event event);

    // ✅ Find all likes for a particular event
    List<Like> findByEvent(Event event);

    // ✅ Delete a like entry when unliking
    void deleteByEventAndUser(Event event, User user);

    Optional<Like> findByEventAndUser(Event event, User user);

    Optional<Like> findByUserAndEvent(User user, Event event);
}

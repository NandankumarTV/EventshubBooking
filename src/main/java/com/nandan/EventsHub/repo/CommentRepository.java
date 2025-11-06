package com.nandan.EventsHub.repo;

import com.nandan.EventsHub.model.Comment;
import com.nandan.EventsHub.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    long countByEvent(Event event);

    List<Comment> findByEventOrderByCreatedAtDesc(Event event);

    List<Comment> findByEvent(Event event);


    Optional<Comment> findById(Long commentId);
}

package com.nandan.EventsHub.repo;

import com.nandan.EventsHub.model.Booking;
import com.nandan.EventsHub.model.Event;
import com.nandan.EventsHub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserUsernameOrderByEventEventDateTimeDesc(String username);

    Optional<Booking> findByTicketId(String ticketId);
}

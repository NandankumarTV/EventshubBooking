package com.nandan.EventsHub.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String eventName;


    private String imageUrl;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private String eventType; // e.g. "Music", "Sports", "Conference"

    @Column(nullable = false)
    private LocalDateTime eventDateTime; // both date and time

    @Column(nullable = false)
    private int totalSeats;

    @Column(nullable = false)
    private int availableSeats;

    @Column(nullable = false)
    private double price;
}

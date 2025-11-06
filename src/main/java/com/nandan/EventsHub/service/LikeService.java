package com.nandan.EventsHub.service;

import com.nandan.EventsHub.dto.LikeActionResponseDTO;
import com.nandan.EventsHub.dto.LikeDTO;
import com.nandan.EventsHub.exception.ResourceNotFoundException;
import com.nandan.EventsHub.model.Event;
import com.nandan.EventsHub.model.Like;
import com.nandan.EventsHub.model.User;
import com.nandan.EventsHub.repo.EventRepository;
import com.nandan.EventsHub.repo.LikeRepository;
import com.nandan.EventsHub.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

//    public List<LikeDTO> getLikesByEventName(String eventName) {
//        Event event = eventRepository.findByEventName(eventName)
//                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventName));
//
//        List<Like> likes = likeRepository.findByEvent_EventName(event.getEventName());
//
//        return likes.stream()
//                .map(like -> LikeDTO.builder()
//                        .username(like.getUser().getUsername())
//                        .build())
//                .collect(Collectors.toList());
//    }

    // Optional: count likes
    public long getLikesCount(String eventName) {
        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventName));
        return likeRepository.countByEvent(event);
    }

//    public LikeActionResponseDTO toggleLike(String username, String eventName) {
//        // Fetch user
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
//
//        // Fetch event
//        Event event = eventRepository.findByEventName(eventName)
//                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventName));
//
//        // Check if already liked
//        boolean alreadyLiked = likeRepository.existsByEvent_EventNameAndUser(event.getEventName(), user);
//        if (alreadyLiked) {
//            // Unlike
//            likeRepository.findByEvent_EventName(event.getEventName()).stream()
//                    .filter(l -> l.getUser().equals(user))
//                    .findFirst()
//                    .ifPresent(likeRepository::delete);
//            return LikeActionResponseDTO.builder()
//                    .message("Event unliked successfully")
//                    .liked(false)
//                    .build();
//        } else {
//            // Like
//            Like like = Like.builder()
//                    .user(user)
//                    .event(event)
//                    .build();
//            likeRepository.save(like);
//            return LikeActionResponseDTO.builder()
//                    .message("Event liked successfully")
//                    .liked(true)
//                    .build();
//        }
//    }

    @Transactional
    public LikeToggleResponse toggleLike(String username, String eventName) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Event event = eventRepository.findByEventName(eventName).orElseThrow(() -> new RuntimeException("Event not found"));

        Like like = (Like) likeRepository.findByUserAndEvent(user, event).orElse(null);
        boolean liked;

        if (like == null) {
            like = Like.builder().user(user).event(event).build();
            likeRepository.save(like);
            liked = true;
        } else {
            likeRepository.delete(like);
            liked = false;
        }

        long likesCount = likeRepository.countByEvent(event);
        return new LikeToggleResponse(true, liked, likesCount);
    }

    public static class LikeToggleResponse {
        public boolean success;
        public boolean liked;
        public long likesCount;

        public LikeToggleResponse(boolean success, boolean liked, long likesCount) {
            this.success = success;
            this.liked = liked;
            this.likesCount = likesCount;
        }
    }


}

package com.nandan.EventsHub.controller;

import com.nandan.EventsHub.dto.LikeActionDTO;
import com.nandan.EventsHub.dto.LikeActionResponseDTO;
import com.nandan.EventsHub.dto.LikeDTO;
import com.nandan.EventsHub.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;


    @GetMapping("/count")
    public ResponseEntity<Long> getLikesCount(@RequestParam String eventName) {
        long count = likeService.getLikesCount(eventName);
        return ResponseEntity.ok(count);
    }

}

package com.nandan.EventsHub.controller;
import com.nandan.EventsHub.dto.CommentDTO;
import com.nandan.EventsHub.dto.CreateCommentDTO;
import com.nandan.EventsHub.dto.DeleteCommentDTO;
import com.nandan.EventsHub.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
}


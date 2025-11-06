package com.nandan.EventsHub.service;

import com.nandan.EventsHub.dto.CommentDTO;
import com.nandan.EventsHub.dto.CreateCommentDTO;
import com.nandan.EventsHub.dto.DeleteCommentDTO;
import com.nandan.EventsHub.exception.ResourceNotFoundException;
import com.nandan.EventsHub.exception.UnauthorizedActionException;
import com.nandan.EventsHub.model.Comment;
import com.nandan.EventsHub.model.Event;
import com.nandan.EventsHub.model.User;
import com.nandan.EventsHub.repo.CommentRepository;
import com.nandan.EventsHub.repo.EventRepository;
import com.nandan.EventsHub.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CommentRepository commentRepository;

    private UserRepository userRepository;







}


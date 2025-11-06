package com.nandan.EventsHub.controller;

import com.nandan.EventsHub.dto.ApiResponseDTO;
import com.nandan.EventsHub.dto.UserNameDTO;
import com.nandan.EventsHub.dto.UserRegisterDTO;
import com.nandan.EventsHub.model.User;
import com.nandan.EventsHub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 1️⃣ Live username check
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Object>> checkUsername(@RequestParam String username) {
        boolean available = userService.isUsernameAvailable(username);
        Map<String, Object> response = new HashMap<>();
        response.put("available", available);
        response.put("message", available ? "Username is available" : "Username already taken");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO> registerUser(@RequestBody UserRegisterDTO dto) {
        User user = userService.registerUser(dto);
        return ResponseEntity.ok(new ApiResponseDTO("User registered successfully with username: " + user.getUsername(), true));
    }

    @GetMapping("/name")
    public ResponseEntity<UserNameDTO> getUserNameByUsername(@RequestParam String username) {
        UserNameDTO userNameDTO = userService.getNameByUsername(username);
        return ResponseEntity.ok(userNameDTO);
    }
}

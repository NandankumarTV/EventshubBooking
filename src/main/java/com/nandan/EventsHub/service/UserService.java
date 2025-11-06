package com.nandan.EventsHub.service;



import com.nandan.EventsHub.dto.UserNameDTO;
import com.nandan.EventsHub.dto.UserRegisterDTO;
import com.nandan.EventsHub.exception.ResourceAlreadyExistsException;
import com.nandan.EventsHub.exception.ResourceNotFoundException;
import com.nandan.EventsHub.model.User;
import com.nandan.EventsHub.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Check if username is available
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }


    public User registerUser(UserRegisterDTO dto) {
        // Check if username already exists
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already taken: " + dto.getUsername());
        }

        // Build user (without encoding password)
        User user = User.builder()
                .username(dto.getUsername())
                .name(dto.getName())
                .password(dto.getPassword()) // storing plain password
                .build();

        return userRepository.save(user);
    }

    public UserNameDTO getNameByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        return UserNameDTO.builder()
                .name(user.getName())
                .build();
    }
}

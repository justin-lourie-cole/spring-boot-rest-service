package com.example.restservice;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import com.example.restservice.ResourceNotFoundException;
import com.example.restservice.User;
import com.example.restservice.UserRepository;

@RestController
@RequestMapping("/api/v1")
public class UserController {
  @Autowired
  private UserRepository userRepository;

  // Get all users
  @GetMapping("/users")
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  // Get a single user by their email
  @PostMapping("/users/email")
  public ResponseEntity<User> getUserByEmail(@Validated @RequestBody User user) throws ResourceNotFoundException {
    User userFound = userRepository.findByEmail(user.getEmail())
        .orElseThrow(() -> new ResourceNotFoundException("User not found for this email :: " + user.getEmail()));
    return ResponseEntity.ok().body(userFound);
  }

  // Create a new user
  @PostMapping("/users")
  public User createUser(@Validated @RequestBody User user) {
    return userRepository.save(user);
  }

  // Update a users email
  @PutMapping("/users/{id}")
  public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
      @Validated @RequestBody User userDetails) throws ResourceNotFoundException {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

    user.setEmail(userDetails.getEmail());
    final User updatedUser = userRepository.save(user);
    return ResponseEntity.ok(updatedUser);
  }

  // Delete a user
  @DeleteMapping("/users/{id}")
  public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId)
      throws ResourceNotFoundException {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

    userRepository.delete(user);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
  }
}

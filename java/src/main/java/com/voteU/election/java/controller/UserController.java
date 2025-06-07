package com.voteU.election.java.controller;

import com.voteU.election.java.dto.LoginDTO;
import com.voteU.election.java.entities.User;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * UserController is a REST controller that manages user-related operations.
 * It provides endpoints for creating, retrieving, updating, and deleting users,
 * as well as performing user-specific queries such as authentication or
 * retrieving users by their username, email, or ID.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a list of all users.
     *
     * @return a ResponseEntity containing a list of all User objects.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Creates a new user in the system.
     *
     * @param user the user object containing the details of the user to be created
     * @return a ResponseEntity containing the created user object
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user to retrieve
     * @return a ResponseEntity containing the retrieved User object if found
     * @throws ResourceNotFoundException if the user with the specified id is not found
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve
     * @return a ResponseEntity containing the User object if found, or throws a NotFound exception
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + username));
    }

    /**
     * Authenticates a user based on the provided login credentials.
     * The method checks the username and password against the stored user data and
     * returns the authenticated user if the credentials are valid. If the credentials
     * are invalid, an exception is thrown.
     *
     * @param loginDto the login credentials containing the username and password
     * @return a ResponseEntity containing the authenticated User if login is successful
     * @throws ResourceNotFoundException if the provided credentials are invalid
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginDTO loginDto) {
        return userService.getUserByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword())
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"));
    }

    /**
     * Retrieves a user by their email address.
     * If no user is found with the specified email, a NotFound exception is thrown.
     *
     * @param email the email address of the user to retrieve
     * @return a ResponseEntity containing the User if found
     * @throws ResourceNotFoundException if no user is found with the given email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));
    }

    /**
     * Updates an existing user with the provided details.
     * The method identifies the user by the given unique identifier and replaces
     * their current data with the provided updated information.
     *
     * @param id the unique identifier of the user to update
     * @param updatedUser the updated user object containing new details for the user
     * @return a ResponseEntity containing the updated User object
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the unique identifier of the user to be deleted
     * @return {@code ResponseEntity<Void>} indicating the operation was successful and no content is returned
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

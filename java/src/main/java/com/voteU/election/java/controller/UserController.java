package com.voteU.election.java.controller;

import com.voteU.election.java.dto.LoginDTO;
import com.voteU.election.java.entities.User;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


/**
 * UserController is a REST controller that manages user-related operations.
 * It provides endpoints for creating, retrieving, updating, and deleting users,
 * as well as performing user-specific queries such as authentication or
 * retrieving users by their username, email, or ID.
 */
@Slf4j
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
    @Operation(summary = "Retrieve all users", description = "Fetch the complete list of all users.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
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
    @Operation(summary = "Create a new user", description = "Add a new user to the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User successfully created."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
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
    @Operation(summary = "Retrieve user by ID", description = "Fetch a user based on their unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Integer id) {

        Optional<User> user = userService.getUserById(id);

        return ResponseEntity.ok(user);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve
     * @return a ResponseEntity containing the User object if found, or throws a NotFound exception
     */
    @Operation(summary = "Retrieve user by username", description = "Fetch a user by their unique username.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<Optional<User>> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);

        return ResponseEntity.ok(user);
    }

    /**
     * Retrieves a user by their email address.
     * If no user is found with the specified email, a NotFound exception is thrown.
     *
     * @param email the email address of the user to retrieve
     * @return a ResponseEntity containing the User if found
     */
    @Operation(summary = "Retrieve user by email", description = "Fetch a user based on their email address.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<Optional<User>> getUserByEmail(@PathVariable String email) {

        Optional<User> user = userService.getUserByEmail(email);

        return ResponseEntity.ok(user);
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
    @Operation(summary = "Update an existing user", description = "Modify the details of an existing user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully."),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    @PutMapping("/id/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the unique identifier of the user to be deleted
     * @return {@code ResponseEntity<Void>} indicating the operation was successful and no content is returned
     */
    @Operation(summary = "Delete a user", description = "Remove a user by their unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id, Authentication authentication) {
        userService.deleteUser(id);
        log.info("Deleting user ID: " + id);
        log.info("Request made by: " + authentication.getName());
        log.info("Authorities: " + authentication.getAuthorities());
        return ResponseEntity.noContent().build();
    }
}

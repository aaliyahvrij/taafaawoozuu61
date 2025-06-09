package com.voteU.election.java.services;

import com.voteU.election.java.entities.Role;
import com.voteU.election.java.entities.User;
import com.voteU.election.java.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


/**
 * Service class for managing User entities. This class provides methods for
 * creating, retrieving, updating, and deleting user records in the database.
 * It interacts with the UserRepository to facilitate data operations.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves a list of all User entities from the database.
     *
     * @return a list of User objects representing all users in the system.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Creates a new user in the system. The method sets the current timestamp as the creation time
     * for the user and saves the user entity to the database.
     *
     * @param user the User object to be created and saved in the database
     * @return the created User object after being saved in the database
     */
    public User createUser(User user) {
        user.setCreatedAt(Instant.now());
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user to retrieve
     * @return an Optional containing the User if found, or an empty Optional if no user exists with the specified id
     */
    public Optional<User> getUserById(Integer id) {
        return userRepository.findUserById(id);
    }

    /**
     * Retrieves a user entity based on the provided username.
     *
     * @param username the username of the user to retrieve
     * @return an Optional containing the User entity if found, or an empty Optional if no user was found
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    /**
     * Retrieves a user from the database based on the provided email.
     *
     * @param email the email address of the user to retrieve
     * @return an Optional containing the User if found, or an empty Optional if no user
     *         exists with the specified email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    /**
     * Updates a user's information in the system. This method searches for a user based on
     * the provided unique identifier, updates their details with the values provided in the
     * updatedUser object, and saves the changes.
     *
     * @param id the unique identifier of the user to update
     * @param updatedUser the User object containing updated information for the user
     * @return the updated User object after being saved in the database
     * @throws RuntimeException if the user with the given ID is not found
     */
    public User updateUser(Integer id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(updatedUser.getUsername());
                    user.setEmail(updatedUser.getEmail());
                    user.setFirstName(updatedUser.getFirstName());
                    user.setLastName(updatedUser.getLastName());
                    user.setGender(updatedUser.getGender());
                    user.setCountry(updatedUser.getCountry());
                    user.setPassword(updatedUser.getPassword());
                    return userRepository.save(user);
                }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Deletes a user from the database by their unique identifier.
     *
     * @param id the unique identifier of the user to be deleted
     */
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}

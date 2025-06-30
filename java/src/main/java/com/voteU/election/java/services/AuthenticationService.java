package com.voteU.election.java.services;

import com.voteU.election.java.dto.JwtResponse;
import com.voteU.election.java.dto.LoginDTO;
import com.voteU.election.java.entities.User;
import com.voteU.election.java.exceptions.AccessDeniedException;
import com.voteU.election.java.repositories.UserRepository;
import com.voteU.election.java.services.JWT.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    /**
     * Handles data access and persistence operations for User entities.
     * This variable is a reference to the UserRepository interface, which provides
     * methods for CRUD operations and custom queries related to User objects.
     * It is primarily used by the service layer to interact with the database.
     */
    private final UserRepository userRepository;
    /**
     * An instance of a PasswordEncoder used to handle password hashing and verification.
     * It provides functionalities such as encoding plain text passwords into secure hashed formats
     * and validating plain text passwords against stored hashed passwords.
     *
     * This variable is a critical component in ensuring secure password management within the
     * authentication process of the application.
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * The JwtService instance used to handle operations related to JSON Web Tokens (JWT),
     * such as generating, validating, and extracting information from tokens.
     *
     * This service is responsible for securely managing the signing and verification
     * of JWTs to facilitate user authentication and authorization.
     *
     * Used primarily in the authentication process to generate tokens
     * for authenticated users and validate tokens for secured requests.
     */
    private final JwtService jwtService;

    /**
     * Creates a new instance of AuthenticationService that provides
     * authentication functionality for users, including verifying credentials
     * and generating JWT tokens.
     *
     * @param userRepository the UserRepository instance used for database operations
     *                        to fetch user data.
     * @param passwordEncoder the PasswordEncoder instance used for verifying
     *                        user passwords.
     * @param jwtService      the JwtService instance used for generating and
     *                        validating JWT tokens.
     */
    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Authenticates a user based on the provided login credentials.
     *
     * @param request an instance of {@code LoginDTO} containing the username and password for authentication
     * @return an instance of {@code JwtResponse} containing the generated JWT token upon successful authentication
     * @throws UsernameNotFoundException if the username provided in the request does not correspond to an existing user
     * @throws AccessDeniedException if the user is blocked
     * @throws BadCredentialsException if the provided password does not match the stored password for the user
     */
    public JwtResponse authenticate(LoginDTO request) {
        User user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        if (user.isBlocked()) {
            throw new AccessDeniedException("User is blocked");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        String token = jwtService.generateToken(user);
        return new JwtResponse(token);
    }
}

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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

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

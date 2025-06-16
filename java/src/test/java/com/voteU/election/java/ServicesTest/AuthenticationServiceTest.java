package com.voteU.election.java.ServicesTest;

import com.voteU.election.java.dto.JwtResponse;
import com.voteU.election.java.dto.LoginDTO;
import com.voteU.election.java.entities.User;
import com.voteU.election.java.repositories.UserRepository;
import com.voteU.election.java.services.AuthenticationService;
import com.voteU.election.java.services.JWT.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("admin");
        mockUser.setPassword("hashed-password"); // this is what the DB stores
    }

    @Test
    void testAuthenticate_success() {
        LoginDTO login = new LoginDTO();
        login.setUsername("admin");
        login.setPassword("plaintext-password");

        when(userRepository.findUserByUsername("admin")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("plaintext-password", "hashed-password")).thenReturn(true);
        when(jwtService.generateToken(mockUser)).thenReturn("mock-jwt");

        JwtResponse response = authenticationService.authenticate(login);

        assertNotNull(response);
        assertEquals("mock-jwt", response.getToken());
    }

    @Test
    void testAuthenticate_invalidUsername() {
        LoginDTO login = new LoginDTO();

        login.setUsername("notfound");
        login.setPassword("pass");

        when(userRepository.findUserByUsername("notfound")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.authenticate(login);
        });
    }

    @Test
    void testAuthenticate_invalidPassword() {
        LoginDTO login = new LoginDTO();

        login.setUsername("admin");
        login.setPassword("wrongpass");

        when(userRepository.findUserByUsername("admin")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrongpass", "hashed-password")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            authenticationService.authenticate(login);
        });
    }
}


package com.voteU.election.java.ControllersTest;

import com.voteU.election.java.controller.AuthenticationController;
import com.voteU.election.java.dto.JwtResponse;
import com.voteU.election.java.dto.LoginDTO;
import com.voteU.election.java.services.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void testAuthenticateUser_success() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("password");

        JwtResponse jwtResponse = new JwtResponse("mock-jwt-token");

        when(authenticationService.authenticate(loginDTO)).thenReturn(jwtResponse);

        // Act
        ResponseEntity<JwtResponse> response = authenticationController.authenticateUser(loginDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("mock-jwt-token", response.getBody().getToken());
    }

    @Test
    void testAuthenticateUser_invalidCredentials() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("wrong-password");

        // Simulate that the authentication service throws an exception for invalid login
        when(authenticationService.authenticate(loginDTO))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> {
            authenticationController.authenticateUser(loginDTO);
        });
    }

    @Test
    void testAuthenticateUser_emptyUsername() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("");
        loginDTO.setPassword("password");

        when(authenticationService.authenticate(loginDTO))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> {
            authenticationController.authenticateUser(loginDTO);
        });
    }
}


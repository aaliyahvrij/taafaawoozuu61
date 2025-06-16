package com.voteU.election.java.ControllersTest;

import com.voteU.election.java.controller.UserController;
import com.voteU.election.java.dto.JwtResponse;
import com.voteU.election.java.dto.LoginDTO;
import com.voteU.election.java.entities.User;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getAllUsers_returnsList() {
        List<User> mockUsers = List.of(
                new User() {{
                    setId(1);
                    setUsername("john_doe");
                    setEmail("john@example.com");
                }},
                new User() {{
                    setId(2);
                    setUsername("jane_doe");
                    setEmail("jane@example.com");
                }}
        );
        when(userService.getAllUsers()).thenReturn(mockUsers);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getUserById_found() {
        User user = new User() {{
            setId(1);
            setUsername("john_doe");
            setEmail("john@example.com");
        }};
        when(userService.getUserById(1)).thenReturn(Optional.of(user));

        ResponseEntity<Optional<User>> response = userController.getUserById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
    }

    @Test
    void getUserById_notFound() {
        when(userService.getUserById(99)).thenReturn(Optional.empty());

        ResponseEntity<Optional<User>> response = userController.getUserById(99);

        assertEquals(HttpStatus.OK, response.getStatusCode()); // This is your actual behavior
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void createUser_success() {
        User user = new User();
        user.setId(null);
        user.setUsername("new_user");
        user.setEmail("new@example.com");
        User created = new User();
        created.setId(3);
        created.setUsername("new_user");
        created.setEmail("new@example.com");

        when(userService.createUser(user)).thenReturn(created);

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().getId());
    }

    @Test
    void updateUser_success() {
        User updated = new User();
        updated.setId(1);
        updated.setUsername("updated_user");
        updated.setEmail("update@example.com");
        when(userService.updateUser(1, updated)).thenReturn(updated);

        ResponseEntity<User> response = userController.updateUser(1, updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("updated_user", response.getBody().getUsername());
    }

    @Test
    void deleteUser_success() {
        Authentication auth = mock(Authentication.class);

        when(auth.getName()).thenReturn("admin");

        when(auth.getAuthorities()).thenAnswer(invocation -> List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        ResponseEntity<Void> response = userController.deleteUser(1, auth);

        verify(userService).deleteUser(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }


}


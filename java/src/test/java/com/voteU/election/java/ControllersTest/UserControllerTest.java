package com.voteU.election.java.ControllersTest;

import com.voteU.election.java.controller.UserController;
import com.voteU.election.java.dto.LoginDTO;
import com.voteU.election.java.entities.User;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User dummyUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dummyUser = new User();
        dummyUser.setId(1);
        dummyUser.setUsername("johndoe");
        dummyUser.setEmail("john@example.com");
        dummyUser.setPassword("secret");
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Collections.singletonList(dummyUser);
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("johndoe", response.getBody().get(0).getUsername());
    }

    @Test
    void testGetUserById_found() {
        when(userService.getUserById(1)).thenReturn(Optional.of(dummyUser));

        ResponseEntity<User> response = userController.getUserById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("johndoe", response.getBody().getUsername());
    }

    @Test
    void testGetUserById_notFound() {
        when(userService.getUserById(2)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userController.getUserById(2));
    }

    @Test
    void testCreateUser() {
        when(userService.createUser(any(User.class))).thenReturn(dummyUser);

        ResponseEntity<User> response = userController.createUser(dummyUser);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("johndoe", response.getBody().getUsername());
    }

    @Test
    void testLogin_success() {
        LoginDTO login = new LoginDTO();

        when(userService.getUserByUsernameAndPassword("johndoe", "secret"))
                .thenReturn(Optional.of(dummyUser));

        ResponseEntity<User> response = userController.login(login);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("johndoe", response.getBody().getUsername());
    }

    @Test
    void testLogin_fail() {
        LoginDTO login = new LoginDTO();

        when(userService.getUserByUsernameAndPassword("johndoe", "wrong"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userController.login(login));
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).deleteUser(1);

        ResponseEntity<Void> response = userController.deleteUser(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    void testUpdateUser() {
        when(userService.updateUser(eq(1), any(User.class))).thenReturn(dummyUser);

        ResponseEntity<User> response = userController.updateUser(1, dummyUser);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("johndoe", response.getBody().getUsername());
    }
}

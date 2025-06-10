package com.voteU.election.java.ServicesTest;

import com.voteU.election.java.entities.User;
import com.voteU.election.java.repositories.UserRepository;
import com.voteU.election.java.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void testGetAllUsers_returnsListOfUsers() {
        List<User> mockUsers = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void testCreateUser_setsCreatedAtAndSavesUser() {
        User input = new User();
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.createUser(input);

        assertNotNull(result.getCreatedAt());
        verify(userRepository).save(input);
    }

    @Test
    void testGetUserByUsername_userExists() {
        User user = new User();
        when(userRepository.findUserByUsername("john")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByUsername("john");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testGetUserByUsernameAndPassword_validCredentials() {
        User user = new User();
        when(userRepository.findUserByUsernameAndPassword("john", "pass")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByUsernameAndPassword("john", "pass");

        assertTrue(result.isPresent());
    }

    @Test
    void testGetUserByEmail_userExists() {
        User user = new User();
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail("test@example.com");

        assertTrue(result.isPresent());
    }

    @Test
    void testUpdateUser_userExists_updatesAndSaves() {
        User existing = new User();
        existing.setUsername("old");

        User updated = new User();
        updated.setUsername("new");

        when(userRepository.findById(1)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1, updated);

        assertEquals("new", result.getUsername());
        verify(userRepository).save(existing);
    }

    @Test
    void testUpdateUser_userNotFound_throwsException() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(999, new User()));
    }

    @Test
    void testDeleteUser_deletesSuccessfully() {
        userService.deleteUser(1);

        verify(userRepository).deleteById(1);
    }


}

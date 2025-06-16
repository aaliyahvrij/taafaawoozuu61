package com.voteU.election.java.ServicesTest;

import com.voteU.election.java.entities.Role;
import com.voteU.election.java.entities.User;
import com.voteU.election.java.exceptions.ResourceAlreadyExistsException;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.repositories.UserRepository;
import com.voteU.election.java.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers_returnsUserList() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void createUser_success() {
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("plaintext");

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("plaintext")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User created = userService.createUser(user);

        assertEquals("hashed", created.getPassword());
        assertEquals(Role.USER, created.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void createUser_existingUsername_throwsException() {
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@example.com");

        when(userRepository.existsByUsername("john")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.createUser(user));
    }

    @Test
    void getUserById_validId_returnsUser() {
        User user = new User();
        when(userRepository.findUserById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1);

        assertTrue(result.isPresent());
    }

    @Test
    void getUserById_nullId_throwsException() {
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(null));
    }

    @Test
    void getUserByUsername_validUsername_returnsUser() {
        when(userRepository.findUserByUsername("jane")).thenReturn(Optional.of(new User()));

        Optional<User> result = userService.getUserByUsername("jane");

        assertTrue(result.isPresent());
    }

    @Test
    void getUserByUsername_null_throwsException() {
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByUsername(null));
    }

    @Test
    void updateUser_userExists_updatesFields() {
        User existing = new User();
        User updated = new User();
        updated.setUsername("new");
        updated.setEmail("new@example.com");
        updated.setPassword("pass");
        updated.setFirstName("First");
        updated.setLastName("Last");
        updated.setCountry("US");
        updated.setGender("Other");

        when(userRepository.findById(1)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser(1, updated);

        assertEquals("new", result.getUsername());
    }

    @Test
    void updateUser_userNotFound_throwsException() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(99, new User()));
    }

    @Test
    void deleteUser_callsRepository() {
        userService.deleteUser(5);

        verify(userRepository).deleteById(5);
    }
}

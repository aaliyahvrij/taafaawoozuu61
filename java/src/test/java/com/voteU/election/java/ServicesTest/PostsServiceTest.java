package com.voteU.election.java.ServicesTest;

import com.voteU.election.java.entities.Posts;
import com.voteU.election.java.entities.User;
import com.voteU.election.java.repositories.PostsRepository;
import com.voteU.election.java.repositories.UserRepository;
import com.voteU.election.java.services.PostsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class PostsServiceTest {

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostsService postsService;

    @Test
    void testCreatePost() {
        // Arrange
        User user = new User();
        user.setId(1);
        Posts post = new Posts();
        post.setUser(user);

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(postsRepository.save(Mockito.any(Posts.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Posts savedPost = postsService.createPost(post);

        // Assert
        assertEquals(user, savedPost.getUser());
        assertNotNull(savedPost.getCreatedAt());
    }
}

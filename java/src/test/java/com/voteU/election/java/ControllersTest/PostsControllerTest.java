package com.voteU.election.java.ControllersTest;

import com.voteU.election.java.controller.PostsController;
import com.voteU.election.java.entities.Posts;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.services.PostsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostsControllerTest {

    @Mock
    private PostsService postsService;

    @InjectMocks
    private PostsController postsController;

    private Posts samplePost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        samplePost = new Posts();
        samplePost.setId(1);
        samplePost.setTitle("Test Post");
        samplePost.setBody("This is a test post content.");
    }

    @Test
    void testGetAllPosts() {
        List<Posts> postsList = Collections.singletonList(samplePost);
        when(postsService.getAllPosts()).thenReturn(postsList);

        ResponseEntity<List<Posts>> response = postsController.getAllPosts();

        assertEquals(1, response.getBody().size());
        assertEquals("Test Post", response.getBody().get(0).getTitle());
        verify(postsService, times(1)).getAllPosts();
    }

    @Test
    void testCreatePost() {
        when(postsService.createPost(any(Posts.class))).thenReturn(samplePost);

        ResponseEntity<Posts> response = postsController.createPost(samplePost);

        assertNotNull(response.getBody());
        assertEquals("Test Post", response.getBody().getTitle());
        verify(postsService, times(1)).createPost(samplePost);
    }

    @Test
    void testGetPostById_Found() {
        when(postsService.getPostById(1)).thenReturn(Optional.of(samplePost));

        ResponseEntity<Posts> response = postsController.getPostById(1);

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
        verify(postsService, times(1)).getPostById(1);
    }

    @Test
    void testGetPostById_NotFound() {
        when(postsService.getPostById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            postsController.getPostById(1);
        });

        assertEquals("Post not found with id 1", exception.getMessage());
        verify(postsService, times(1)).getPostById(1);
    }

    @Test
    void testDeletePost() {
        doNothing().when(postsService).deletePost(1);

        ResponseEntity<Void> response = postsController.deletePost(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(postsService, times(1)).deletePost(1);
    }
}

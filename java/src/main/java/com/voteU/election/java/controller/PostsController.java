package com.voteU.election.java.controller;

import com.voteU.election.java.entities.Posts;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.services.PostsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostsService postsService;

    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    /**
     * Retrieves a list of all posts.
     *
     * @return a ResponseEntity containing a list of all Posts objects.
     */
    @GetMapping
    public ResponseEntity<List<Posts>> getAllPosts() {
        return ResponseEntity.ok(postsService.getAllPosts());
    }

    /**
     * Creates a new post in the application and persists it to the database.
     *
     * @param post the Posts object containing the details of the post to be created
     * @return a ResponseEntity containing the newly created Posts object
     */
    @PostMapping
    public ResponseEntity<Posts> createPost(Posts post) {
        return ResponseEntity.ok(postsService.createPost(post));
    }

    /**
     * Retrieves a post by its unique identifier.
     *
     * @param id the unique identifier of the post to retrieve
     * @return a ResponseEntity containing the retrieved Posts object if found,
     *         or throws an exception if no post is found with the specified id
     * @throws ResourceNotFoundException if the post with the specified id is not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Posts> getPostById(@PathVariable Integer id) {
        return postsService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + id));
    }

    /**
     * Deletes a post by its unique identifier.
     * The method removes the specified post from the system if it exists.
     *
     * @param id the unique identifier of the post to be deleted
     * @return {@code ResponseEntity<Void>} indicating the operation was successful and no content is returned
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        postsService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}

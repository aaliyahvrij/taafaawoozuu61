package com.voteU.election.java.controller;

import com.voteU.election.java.entities.Posts;
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

    @GetMapping
    public ResponseEntity<List<Posts>> getAllPosts() {
        return ResponseEntity.ok(postsService.getAllPosts());
    }

    @PostMapping
    public ResponseEntity<Posts> createPost(Posts post) {
        return ResponseEntity.ok(postsService.createPost(post));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Posts> getPostById(@PathVariable Integer id) {
        return postsService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Post not found with id " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        postsService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}

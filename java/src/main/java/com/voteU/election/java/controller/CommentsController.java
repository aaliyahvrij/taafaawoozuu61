package com.voteU.election.java.controller;

import com.voteU.election.java.entities.Comments;
import com.voteU.election.java.entities.Posts;
import com.voteU.election.java.entities.User;
import com.voteU.election.java.exceptions.NotFound;
import com.voteU.election.java.services.CommentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentsController {

    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @GetMapping
    public ResponseEntity<List<Comments>> getAllComments() {
        return ResponseEntity.ok(commentsService.getAllComments());
    }

    @PostMapping
    public ResponseEntity<Comments> createComment(@RequestBody Comments comment) {
        return ResponseEntity.ok(commentsService.createComment(comment));
    }

    @GetMapping("/post/{postsId}")
    public ResponseEntity<List<Comments>> getCommentsByPostId(@PathVariable Posts postsId) {
        List<Comments> comments = commentsService.getCommentsByPostsId(postsId);
        if (comments.isEmpty()) {
            throw new NotFound("No comments found for postId: " + postsId);
        }
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comments>> getCommentsByUserId(@PathVariable User userId) {
        List<Comments> comments = commentsService.getCommentsByUserId(userId);
        if (comments.isEmpty()) {
            throw new NotFound("No comments found for userId: " + userId);
        }
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/comments/{commentsId}")
    public ResponseEntity<List<Comments>> getCommentsByCommentsId(@PathVariable Integer commentsId) {
        List<Comments> comments = commentsService.getCommentsByCommentsId(commentsId);
        if (comments.isEmpty()) {
            throw new NotFound("No comments found for commentsId: " + commentsId);
        }
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        commentsService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

}

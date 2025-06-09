package com.voteU.election.java.controller;

import com.voteU.election.java.entities.Comments;
import com.voteU.election.java.entities.Posts;
import com.voteU.election.java.entities.User;
import com.voteU.election.java.exceptions.ResourceNotFoundException;
import com.voteU.election.java.services.CommentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controller class for managing comments-related operations.
 * Provides endpoints to create, retrieve, and delete comments.
 * The controller interacts with the CommentsService to perform the business logic.
 */
@RestController
@RequestMapping("/api/comments")
public class CommentsController {

    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    /**
     * Retrieves all comments.
     *
     * @return a {@code ResponseEntity} containing a list of all comments.
     */
    @GetMapping
    public ResponseEntity<List<Comments>> getAllComments() {
        return ResponseEntity.ok(commentsService.getAllComments());
    }

    /**
     * Creates a new comment in the system.
     *
     * @param comment the comment object to be created. This includes details such as
     *                the comment body, associated user, post, or parent comment.
     * @return a ResponseEntity containing the created comment object.
     */
    @PostMapping
    public ResponseEntity<Comments> createComment(@RequestBody Comments comment) {
        return ResponseEntity.ok(commentsService.createComment(comment));
    }

    /**
     * Retrieves a list of comments associated with a specific post.
     * If no comments are found for the given post ID, a NotFound exception is thrown.
     *
     * @param postsId the post entity whose associated comments are to be retrieved
     * @return a ResponseEntity containing a list of comments associated with the specified post
     * @throws ResourceNotFoundException if no comments are found for the given post ID
     */
    @GetMapping("/posts/{postsId}")
    public ResponseEntity<List<Comments>> getCommentsByPostId(@PathVariable Integer postsId) {
        Posts post = new Posts();
        post.setId(postsId);
        List<Comments> comments = commentsService.getCommentsByPostsId(post);
        if (comments.isEmpty()) {
            throw new ResourceNotFoundException("No comments found for postId: " + postsId);
        }
        return ResponseEntity.ok(comments);
    }

    /**
     * Retrieves a list of comments associated with a specific user.
     * Throws a {@link ResourceNotFoundException} exception if no comments are found for the given user ID.
     *
     * @param userId The user entity whose associated comments are to be retrieved.
     * @return A {@link ResponseEntity} containing a list of {@link Comments} associated with the user.
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Comments>> getCommentsByUserId(@PathVariable User userId) {
        List<Comments> comments = commentsService.getCommentsByUserId(userId);
        if (comments.isEmpty()) {
            throw new ResourceNotFoundException("No comments found for userId: " + userId);
        }
        return ResponseEntity.ok(comments);
    }

    /**
     * Retrieves a list of comments that are associated with the specified parent comment ID.
     * If no comments are found for the given ID, a NotFound exception is thrown.
     *
     * @param commentsId the ID of the parent comment whose associated comments are to be retrieved
     * @return a ResponseEntity containing the list of comments associated with the specified comment ID
     *         or an HTTP status code indicating the result of the operation
     */
    @GetMapping("/commentsId/{commentsId}")
    public ResponseEntity<List<Comments>> getCommentsByCommentsId(@PathVariable Integer commentsId) {
        List<Comments> comments = commentsService.getCommentsByCommentsId(commentsId);
        if (comments.isEmpty()) {
            throw new ResourceNotFoundException("No comments found for commentsId: " + commentsId);
        }
        return ResponseEntity.ok(comments);
    }

    /**
     * Deletes a comment identified by its unique ID.
     * This method interacts with the CommentsService to remove the comment from the database.
     *
     * @param id the unique identifier of the comment to be deleted
     * @return ResponseEntity with no content if the deletion is successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        commentsService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

}

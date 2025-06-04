package com.voteU.election.java.services;

import com.voteU.election.java.entities.Comments;
import com.voteU.election.java.entities.Posts;
import com.voteU.election.java.entities.User;
import com.voteU.election.java.repositories.CommentsRepository;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service class responsible for managing operations related to comments in the system.
 * It provides methods to retrieve, create, and delete comments, as well as query comments
 * based on associated posts, users, or comment identifiers.
 */
@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;

    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    /**
     * Retrieves a list of all comments from the database.
     *
     * @return a list of Comments objects representing all comments in the system.
     */
    public List<Comments> getAllComments(){
        return commentsRepository.findAll();
    }

    /**
     * Creates and saves a new comment in the database.
     *
     * @param comment the Comments object to be created and saved
     * @return the created Comments object after being saved in the database
     */
    public Comments createComment(Comments comment) {
        return commentsRepository.save(comment);
    }

    /**
     * Retrieves a list of comments associated with the specified post.
     *
     * @param postsId the post for which the comments are to be retrieved
     * @return a list of comments related to the provided post
     */
    public List<Comments> getCommentsByPostsId(Posts postsId){
        return commentsRepository.findByPostsId(postsId);
    }

    /**
     * Retrieves a list of comments associated with a specific user.
     *
     * @param userId the user whose comments are to be retrieved
     * @return a list of Comments objects associated with the specified user
     */
    public List<Comments> getCommentsByUserId(User userId){
        return commentsRepository.findByUserId(userId);
    }

    /**
     * Retrieves a list of comments associated with the specified comment ID.
     *
     * @param commentsId the unique identifier of the comment whose associated comments are to be retrieved
     * @return a list of Comments objects associated with the specified comment ID
     */
    public List<Comments> getCommentsByCommentsId(Integer commentsId){
        return commentsRepository.findByCommentsId(commentsId);
    }

    /**
     * Deletes a comment from the database using its unique identifier.
     *
     * @param id the unique identifier of the comment to be deleted
     */
    public void deleteComment(Integer id){
        commentsRepository.deleteById(id);
    }
}

package com.voteU.election.java.services;

import com.voteU.election.java.entities.Posts;
import com.voteU.election.java.entities.User;
import com.voteU.election.java.repositories.PostsRepository;
import com.voteU.election.java.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


/**
 * Service class for managing Posts entities. This class provides methods for creating,
 * retrieving, and deleting posts in the system. It interacts with the PostsRepository
 * to facilitate data operations.
 */
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    public PostsService(PostsRepository postsRepository, UserRepository userRepository) {
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a list of all posts from the database.
     *
     * @return a list of Posts objects representing all posts in the system.
     */
    public List<Posts> getAllPosts(){
        return postsRepository.findAll();
    }



    /**
     * Creates a new post in the system. This method sets the current timestamp as the creation time
     * for the post and saves the post entity to the database.
     *
     * @param post the Posts object to be created and saved in the database
     * @return the created Posts object after being saved in the database
     */
    public Posts createPost(Posts post) {
        Integer userId = post.getUser().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        post.setUser(user);
        post.setCreatedAt(Instant.now());
        return postsRepository.save(post);
    }

    /**
     * Retrieves a specific post by its unique identifier.
     *
     * @param id the unique identifier of the post to retrieve
     * @return an Optional containing the Posts entity if found, or an empty Optional if no post exists with the specified id
     */
    public Optional<Posts> getPostById(Integer id){
        return postsRepository.findPostsById(id);
    }

    /**
     * Deletes a post identified by its unique identifier.
     *
     * @param id the unique identifier of the post to be deleted
     */
    public void deletePost(Integer id){
        postsRepository.deleteById(id);
    }
}

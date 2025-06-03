package com.voteU.election.java.services;

import com.voteU.election.java.entities.Posts;
import com.voteU.election.java.repositories.PostsRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PostsService {

    private final PostsRepository postsRepository;

    public PostsService(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    public List<Posts> getAllPosts(){
        return postsRepository.findAll();
    }

    public Posts createPost(Posts post) {
        post.setCreatedAt(Instant.now());
        return postsRepository.save(post);
    }

    public Optional<Posts> getPostById(Integer id){
        return postsRepository.findPostsById(id);
    }

    public void deletePost(Integer id){
        postsRepository.deleteById(id);
    }
}

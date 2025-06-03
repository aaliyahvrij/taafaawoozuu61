package com.voteU.election.java.repositories;

import com.voteU.election.java.entities.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Integer> {

    Optional<Posts> findPostsById(Integer id);

    void deleteById(Integer id);
}

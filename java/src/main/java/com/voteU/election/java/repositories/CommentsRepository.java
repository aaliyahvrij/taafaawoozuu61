package com.voteU.election.java.repositories;

import com.voteU.election.java.entities.Comments;
import com.voteU.election.java.entities.Posts;
import com.voteU.election.java.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository interface for managing database operations related to the Comments entity.
 * Extends JpaRepository to provide basic CRUD operations and additional query methods.
 */
@Repository
public interface CommentsRepository extends JpaRepository<Comments, Integer> {

    List<Comments> findByPostsId(Posts postsId);

    List<Comments> findByUserId(User userId);

    List<Comments> findByCommentsId(Integer commentsId);

    void deleteById(Integer id);
}

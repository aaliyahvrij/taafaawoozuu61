package com.voteU.election.java.services;

import com.voteU.election.java.entities.Comments;
import com.voteU.election.java.entities.Posts;
import com.voteU.election.java.entities.User;
import com.voteU.election.java.repositories.CommentsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;

    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    public List<Comments> getAllComments(){
        return commentsRepository.findAll();
    }

    public Comments createComment(Comments comment) {
        return commentsRepository.save(comment);
    }

    public List<Comments> getCommentsByPostsId(Posts postsId){
        return commentsRepository.findByPostsId(postsId);
    }

    public List<Comments> getCommentsByUserId(User userId){
        return commentsRepository.findByUserId(userId);
    }

    public List<Comments> getCommentsByCommentsId(Integer commentsId){
        return commentsRepository.findByCommentsId(commentsId);
    }

    public void deleteComment(Integer id){
        commentsRepository.deleteById(id);
    }
}

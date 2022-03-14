package com.webstore.service;

import com.webstore.domain.CommentVotes;
import com.webstore.repository.CommentVotesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentVotesService {

    @Autowired private CommentVotesRepo commentVotesRepo;

    public List<CommentVotes> getCommentNote(long commentId, long userId){
        return commentVotesRepo.findAllByCommentIdAndUserId(commentId, userId);
    }

    public boolean checkExistsPositiveNote(long commentId, long userId){
        return commentVotesRepo.countCommentVotesByCommentIdAndUserIdAndPositive(commentId, userId, true) > 0;
    }

    public boolean checkExistsNegativeNote(long commentId, long userId){
        return commentVotesRepo.countCommentVotesByCommentIdAndUserIdAndPositive(commentId, userId, false) > 0;
    }



    public CommentVotes save(CommentVotes commentVotes){
        return commentVotesRepo.save(commentVotes);
    }
}

package com.webstore.repository;

import com.webstore.domain.CommentVotes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentVotesRepo extends JpaRepository<CommentVotes, Long> {
    List<CommentVotes> findAllByCommentIdAndUserId(long commentId, long userId);
    int countCommentVotesByCommentIdAndUserIdAndPositive(long commentId, long userId, boolean positive);

}

package com.webstore.service;

import com.webstore.repository.CommentRepo;
import com.webstore.domain.Comment;
import com.webstore.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;

    public List<Comment> getBatchCommentsByIdProduct(long id, int offset, int limit) {
        return commentRepo.findAllByProductIdWithPagination(id, new PageRequest(offset, limit, Sort.Direction.DESC, "id"));
    }

    public List<Comment> getBatchCommentsByIdProduct(long id) {
        return commentRepo.findAllByProductId(id);
    }

    public Long getCountBatchCommentsByIdUser(Long id) {
        return commentRepo.findAllCountByUserId(id);
    }

    public List<Comment> getBatchCommentsByIdUser(long id, int offset, int limit) {
        return commentRepo.findAllByUserIdWithPagination(id, new PageRequest(offset, limit, Sort.Direction.DESC, "id"));
    }

    public int findAllParentCountByProductId(long id) {
        return commentRepo.findAllParentCountByProductId(id);
    }

    public int getCountCommentsByIdProduct(long id) {
        return commentRepo.findAllCountByProductId(id);
    }

    public boolean checkExistsCommentWithStar(long product_id, long user_id) {
        return (commentRepo.getCountCommentWithStarByUserAndProduct(product_id, user_id) > 0);
    }

    public boolean isExistsChild(long parentId) {
        return (commentRepo.existsByParentId(parentId));
    }

    public Comment save(Comment comment) {
        return commentRepo.save(comment);
    }

    public Comment find(long commentId) {
        return commentRepo.findOne(commentId);
    }

    public int getPageNumberOfCommentListByCommentId(int limit, int productId, int commentId) {
        int totalCnt = getCountCommentsByIdProduct(productId);
        List<Comment> allComments = getBatchCommentsByIdProduct(productId);
        Comment searchComment = find(commentId);

        int targetPosition = allComments.indexOf(searchComment);
        if (targetPosition < 0) {
            for (int i = 0; i < allComments.size(); i++) {
                Comment comment = allComments.get(i);
                targetPosition = comment.getChildren().indexOf(searchComment);
                if (targetPosition >= 0) {
                    targetPosition = allComments.indexOf(comment);
                    break;
                }
            }
        }

        for (int i = 0; i < totalCnt; i++)
            if (limit * i > targetPosition)
                return --i;

        return 0;
    }
}

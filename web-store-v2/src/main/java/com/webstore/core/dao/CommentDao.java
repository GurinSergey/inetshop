package com.webstore.core.dao;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Comment;

import java.util.List;

/**
 * Created by DVaschenko on 08.08.2016.
 */
public interface CommentDao extends Crud<Comment> {
    public List<Comment> getComentsLimitByProduct(int begin, int product);
    public void addVoteToCommentByUser(int commentId, int userId);
    public boolean isExistsVoteUser(int commentId, int userId);
}

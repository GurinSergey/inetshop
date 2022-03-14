package com.webstore.core.service;

import com.webstore.core.dao.common.Crud;
import com.webstore.core.entities.Comment;
import com.webstore.core.entities.User;

import java.util.List;

/**
 * Created by DVaschenko on 08.08.2016.
 */
public interface CommentService extends Crud<Comment>{
    public List<Comment> getComments(int start, int product);
    public void setPositiveVoteForComment(User user, Comment comment);
    public void setNegativeVoteForComment(User user, Comment comment);
}

package com.webstore.core.service.impl;

import com.webstore.core.dao.CommentDao;
import com.webstore.core.entities.Comment;
import com.webstore.core.entities.User;
import com.webstore.core.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;



/**
 * Created by DVaschenko on 08.08.2016.
 */
@Service
public class ICommentService implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public Comment add(Comment comment) {
        Comment parent = comment.getParent();
        Comment child = commentDao.add(comment);

        if (parent != null) {
            parent.getChildren().add(child);
            update(parent);
        }

        return child;
    }

    @Override
    public Comment find(Comment item) {
        return null;
    }

    @Override
    public Comment find(int id) {
        return commentDao.find(id);
    }

    @Override
    public boolean delete(Comment item) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Comment update(Comment item) {
        return commentDao.update(item);
    }

    @Override
    public List<Comment> getComments(int start, int product){
        return commentDao.getComentsLimitByProduct(start, product);
    }

    @Override
    public void setPositiveVoteForComment(User user, Comment comment) {
        if (user != null && !commentDao.isExistsVoteUser(comment.getId(), user.getId())) {
            commentDao.addVoteToCommentByUser(comment.getId(), user.getId());
            comment.setPlus(comment.getPlus() + 1);
            update(comment);
        }
    }

    @Override
    public void setNegativeVoteForComment(User user, Comment comment) {
        if (user != null && !commentDao.isExistsVoteUser(comment.getId(), user.getId())) {
            commentDao.addVoteToCommentByUser(comment.getId(), user.getId());
            comment.setMinus(comment.getMinus() + 1);
            update(comment);
        }
    }
}

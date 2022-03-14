package com.webstore.core.dao.impl;

import com.webstore.core.dao.CommentDao;
import com.webstore.core.entities.Comment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

import static com.webstore.core.util.Const.COUNT_COMMENTS_LOAD;

/**
 * Created by DVaschenko on 08.08.2016.
 */
@Repository
public class ICommentDao implements CommentDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Comment add(Comment item) {
        return entityManager.merge(item);
    }

    @Override
    public Comment find(Comment item) {
        return null;
    }

    @Override
    public Comment find(int id) {
        return entityManager.find(Comment.class, id);
    }

    @Override
    @Transactional
    public boolean delete(Comment item) {
        return false;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return false;
    }

    @Override
    @Transactional
    public Comment update(Comment item) {
        return entityManager.merge(item);
    }

    @Override
    public List<Comment> getComentsLimitByProduct(int begin, int product) {
        return entityManager
                .createQuery("select c from Comment c where c.id > :start and c.parent is null and c.product.id = :productId order by c.id")
                .setParameter("start", begin)
                .setParameter("productId", product)
                .setMaxResults(COUNT_COMMENTS_LOAD)
                .getResultList();
    }

    @Override
    @Transactional
    public void addVoteToCommentByUser(int commentId, int userId) {
        entityManager
                .createNativeQuery("INSERT INTO votes_comments VALUES(?, ?)")
                .setParameter(1, userId)
                .setParameter(2, commentId)
                .executeUpdate();
    }

    @Override
    public boolean isExistsVoteUser(int commentId, int userId) {
        return entityManager
                .createNativeQuery("SELECT v.* FROM votes_comments v WHERE v.userId =?1 AND v.commentId =?2")
                .setParameter(1, userId)
                .setParameter(2, commentId)
                .getResultList().size() > 0;
    }
}

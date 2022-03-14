package com.webstore.repository;

import com.webstore.domain.Comment;
import com.webstore.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {
    @Query(nativeQuery = true, value = "select count(*) from Comment where product_id = ?1")
    int findAllCountByProductId(long id);

    @Query(value = "select c from Comment c where c.productId = ?1 and c.parentId = 0")
    List<Comment> findAllByProductIdWithPagination(long id, Pageable pageable);

    @Query(value = "select c from Comment c where c.productId = ?1 and c.parentId = 0 order by id desc")
    List<Comment> findAllByProductId(long id);

    @Query(value = "select count(c) from Comment c where c.userId = ?1")
    Long findAllCountByUserId(Long id);

    @Query(value = "select c from Comment c where c.userId = ?1")
    List<Comment> findAllByUserIdWithPagination(Long id, Pageable pageable);

    @Query(value = "select count(c) from Comment c where c.productId = ?1 and c.parentId = 0")
    int findAllParentCountByProductId(long id);

    @Query(nativeQuery = true, value = "select count(*) from Comment where product_id = ?1 and user_id = ?2 and star_rating <> '0'")
    int getCountCommentWithStarByUserAndProduct(long product_id, long user_id);

    Boolean existsByParentId(long parent_id);
}

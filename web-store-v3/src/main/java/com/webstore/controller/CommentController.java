package com.webstore.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webstore.base.exception.CheckException;
import com.webstore.domain.*;
import com.webstore.domain.json.CommentBasicJson;
import com.webstore.responses.Response;
import com.webstore.security.services.TokenAuthService;
import com.webstore.service.CommentService;
import com.webstore.service.CommentVotesService;
import com.webstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.webstore.util.Utils.getStringFromInputStream;

/**
 * Created by DVaschenko on 20.10.2016.
 */
@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentVotesService votesService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TokenAuthService tokenAuthService;

    @GetMapping("/product/{id}/comments")
    public @ResponseBody
    Response getProductComments(
            @PathVariable("id") long id,
            @RequestParam(value = "offset") Integer offset,
            @RequestParam(value = "limit") Integer limit) {
        try {
            HashMap<String, Object> results = new HashMap<>();
            results.put("count", commentService.findAllParentCountByProductId(id));

            List<Comment> comments = commentService.getBatchCommentsByIdProduct(id, offset, limit);
            results.put("comments", comments);

            User user = tokenAuthService.getCurrentUser();
            if (user == null)
                return Response.ok(results);

            List<CommentVotes> votes = new ArrayList<>();
            comments.forEach(comment -> {
                votes.addAll(votesService.getCommentNote(comment.getId(), user.getId()));
            });
            results.put("notes", votes);
            return Response.ok(results);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("Не удалось загрузить комментарии товара");
        }
    }

    @GetMapping("/product/{id}/comments/count")
    public @ResponseBody
    Response getCountCommentsByProductId(
            @PathVariable("id") long id) {
        return Response.ok(commentService.getCountCommentsByIdProduct(id));
    }

    private Comment saveData(Product p, Comment c) {
        productService.update(p);
        return commentService.save(c);
    }

    @PostMapping(value = "/product/{id}/comment/add")
    @ResponseBody
    public Response addAnswerToComment(
            @PathVariable Long id,
            HttpServletRequest request) {
        try {
            String json = getStringFromInputStream(request.getInputStream());
            JsonNode node = new ObjectMapper().readTree(json);

            Product product = productService.find(id);
            if (product == null)
                throw new CheckException("Отсутствует продукт");

            Comment comment = new Comment();
            comment.setProductId(product.getId());
            comment.setText(node.path("text").asText());
            comment.setParentId(node.path("parent_id").asLong());
            comment.setAuthor(node.path("author").asText());
            product.getStatistics().incrementTotalComment();

            User user = tokenAuthService.getCurrentUser();
            if (user == null)
                return Response.ok(saveData(product, comment));

            comment.setAuthor(user.getPersonName());
            comment.setUserId(user.getId());

            int star = node.path("rating_star").asInt();
            if (!(star > 0 && !commentService.checkExistsCommentWithStar(id, user.getId())))
                return Response.ok(saveData(product, comment));

            comment.setStarRating(star);
            product.getStatistics().calculateTotalRating(star);
            return Response.ok(saveData(product, comment));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("Не удалось сохранить комментарий");
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/product/{product_id}/comment/{comment_id}/like")
    @ResponseBody
    public Response likeComment(
            @PathVariable("product_id") long productId,
            @PathVariable("comment_id") long commentId
    ) {
        try {
            User user = tokenAuthService.getPrincipal().getUser();
            Comment comment = commentService.find(commentId);
            Product product = productService.find(productId);
            if (comment == null)
                throw new CheckException("Отсутствует комментарий");

            if (product == null)
                throw new CheckException("Отсутствует продукт");

            if (votesService.checkExistsPositiveNote(commentId, user.getId()))
                throw new CheckException("Вы уже оставили отзыв");

            long note = comment.getNote();
            comment.setNote(++note);
            commentService.save(comment);

            ProductRating rating = product.getStatistics().getComments_statistics();
            int cnt = rating.getPositiveCnt();
            rating.setPositiveCnt(++cnt);
            productService.update(product);

            votesService.save(new CommentVotes(commentId, user.getId(), true));

            return Response.ok("OK");
        } catch (CheckException e) {
            e.printStackTrace();
            if (e.getMessage() != null)
                return Response.error(e.getMessage());
            return Response.error("Неизвестная ошибка");
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/product/{product_id}/comment/{comment_id}/dislike")
    @ResponseBody
    public Response dislikeComment(
            @PathVariable("product_id") long productId,
            @PathVariable("comment_id") long commentId
    ) {
        try {
            User user = tokenAuthService.getPrincipal().getUser();
            Comment comment = commentService.find(commentId);
            Product product = productService.find(productId);
            if (comment == null)
                throw new CheckException("Отсутствует комментарий");

            if (product == null)
                throw new CheckException("Отсутствует продукт");

            if (votesService.checkExistsNegativeNote(commentId, user.getId()))
                throw new CheckException("Вы уже оставили отзыв");

            long note = comment.getNote();
            comment.setNote(--note);
            commentService.save(comment);

            ProductRating rating = product.getStatistics().getComments_statistics();
            int cnt = rating.getNegativeCnt();
            rating.setNegativeCnt(++cnt);
            productService.update(product);

            votesService.save(new CommentVotes(commentId, user.getId(), false));

            return Response.ok("OK");
        } catch (CheckException e) {
            e.printStackTrace();
            if (e.getMessage() != null)
                return Response.error(e.getMessage());
            return Response.error("Неизвестная ошибка");
        }
    }

    @GetMapping(value = "comments/getAllByCurrentUser")
    @ResponseBody
    public Response getAllByCurrentUser(@RequestParam(value = "offset") Integer offset,
                                        @RequestParam(value = "limit") Integer limit) {
        try {
            User user = tokenAuthService.getCurrentUser();
            if (user == null)
                return Response.ok("Авторизируйтесь, пожалуйста");

            List<Comment> comments = commentService.getBatchCommentsByIdUser(user.getId(), offset, limit);

            List<CommentBasicJson> commentsBasicJson = new ArrayList<>();

            for (Comment comment : comments) {
                CommentBasicJson commentBasicJson = new CommentBasicJson();

                commentBasicJson.setCommentId(comment.getId());
                commentBasicJson.setExistsChild(commentService.isExistsChild(comment.getId()));
                commentBasicJson.setText(comment.getText());
                commentBasicJson.setDate(comment.getDate());
                commentBasicJson.setProductId(comment.getProductId());

                Product product = productService.find(comment.getProductId());
                commentBasicJson.setLatIdent(product.getLatIdent());
                commentBasicJson.setProductPhotoUrl(product.getPhotos().iterator().next().getPath());
                commentBasicJson.setProductTitle(product.getTitle());
                commentBasicJson.setProductCode(product.getCode());

                commentBasicJson.setUserId(comment.getUserId());
                commentBasicJson.setAuthor(comment.getAuthor());

                commentsBasicJson.add(commentBasicJson);
            }

            HashMap<String, Object> results = new HashMap<>();

            results.put("count", commentService.getCountBatchCommentsByIdUser(user.getId()));
            results.put("results", commentsBasicJson);

            return Response.ok(results);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("Не удалось загрузить комментарии пользователя");
        }
    }

    @GetMapping(value = "comments/getOffsetByCommentId")
    @ResponseBody
    public Response getOffsetByCommentId(@RequestParam(value = "limit") Integer limit,
                                         @RequestParam(value = "productId") Integer productId,
                                         @RequestParam(value = "commentId") Integer commentId) {
        return Response.ok(commentService.getPageNumberOfCommentListByCommentId(limit, productId, commentId));
    }
}

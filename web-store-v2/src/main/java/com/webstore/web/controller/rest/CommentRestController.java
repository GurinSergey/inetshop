package com.webstore.web.controller.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webstore.core.base.jsonView.View;
import com.webstore.core.entities.Comment;
import com.webstore.core.entities.Product;
import com.webstore.core.entities.User;
import com.webstore.core.service.CommentService;
import com.webstore.security.services.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.webstore.core.util.Utils.getStringFromInputStream;

/**
 * Created by DVaschenko on 20.10.2016.
 */
@Controller
public class CommentRestController {
    @Autowired
    private CommentService commentService;

    @Autowired private TokenAuthService tokenAuthService;

    @RequestMapping(value = "/product/{idp}/comment/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(value = View.ForCommentView.class)
    public Comment addAnswerToComment(@PathVariable(value = "idp") int idp, HttpServletRequest request) throws IOException {
        String json = getStringFromInputStream(request.getInputStream());
        JsonNode node = new ObjectMapper().readTree(json);

        Comment comment = new Comment();
        comment.setText(node.path("text").asText());
        comment.setProduct(new Product(idp));

        if(node.path("parent").asInt() != 0) {
            Comment parent = commentService.find(node.path("parent").asInt());
            comment.setParent(parent);
        }

        User user = tokenAuthService.getPrincipal();
        if(user == null)
            comment.setAuthor(node.path("author").asText());
        else
            comment.setUser(user);

        return commentService.add(comment);
    }

    @RequestMapping(value = "/comment/{id}/positive", method = RequestMethod.POST)
    @ResponseBody
    public int setPositive(@PathVariable(value = "id") int id){
        Comment comment = commentService.find(id);
        commentService.setPositiveVoteForComment(tokenAuthService.getPrincipal(), comment);
        return comment.getPlus();
    }

    @RequestMapping(value = "/comment/{id}/negative", method = RequestMethod.POST)
    @ResponseBody
    public int setNegative(@PathVariable(value = "id") int id){
        Comment comment = commentService.find(id);
        commentService.setNegativeVoteForComment(tokenAuthService.getPrincipal(), comment);
        return comment.getMinus();
    }
}

package com.rebirth.simplepost.web.controllers;

import com.rebirth.simplepost.domain.entities.Comment;
import com.rebirth.simplepost.services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController<Comment, Long> {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        super(Comment.class);
        this.commentService = commentService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Comment>> getComment() {
        return ResponseEntity.ok(this.commentService.fetchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComments(@PathVariable("id") Long id) {
        Comment comment = this.commentService.fetchById(id)
                .orElseThrow(() -> this.resoruceNotFound(id));
        return ResponseEntity.ok(comment);
    }


    @PostMapping("/")
    public ResponseEntity<Comment> postComment(@RequestBody Comment comment) {
        Comment newComment = this.commentService.create(comment);
        URI uriNewEntity = this.generateNewUri(newComment.getId());
        return ResponseEntity.created(uriNewEntity).body(newComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id) {
        this.commentService.remove(id);
        return ResponseEntity.noContent()
                .header("accepted", "delete id " + id)
                .build();
    }

}

package com.rebirth.simplepost.web.controllers;

import com.rebirth.simplepost.domain.tables.dtos.CommentDto;
import com.rebirth.simplepost.services.CommentService;
import com.rebirth.simplepost.web.errors.BadRequestAlertException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController extends BaseController<CommentDto, Long> {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        super(CommentDto.class);
        this.commentService = commentService;
    }

    @GetMapping("/")
    public ResponseEntity<List<CommentDto>> getComment(@RequestParam(required = false, name = "__f", defaultValue = "") String filtro) {
        return ResponseEntity.ok(this.commentService.fetchAll(filtro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getComments(@PathVariable("id") Long id) {
        CommentDto comment = this.commentService.fetchById(id)
                .orElseThrow(() -> this.resoruceNotFound(id));
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/")
    public ResponseEntity<CommentDto> postComment(@Validated @RequestBody CommentDto comment, Errors errors) {
        this.errorHandler(errors);
        if (comment.getCommentId() != null) {
            throw new BadRequestAlertException("El recurso tiene un id asignado", this.klass.getName(), "ASSIGNED_ID");
        }

        CommentDto newComment = this.commentService.create(comment);
        URI uriNewEntity = this.generateNewUri(newComment.getCommentId());
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

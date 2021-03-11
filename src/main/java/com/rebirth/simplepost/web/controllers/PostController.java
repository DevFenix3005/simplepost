package com.rebirth.simplepost.web.controllers;

import com.rebirth.simplepost.services.CommentService;
import com.rebirth.simplepost.services.PostService;
import com.rebirth.simplepost.services.dtos.CommentDto;
import com.rebirth.simplepost.services.dtos.PostDto;
import com.rebirth.simplepost.web.errors.BadRequestAlertException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController extends BaseController<PostDto, Long> {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        super(PostDto.class);
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public ResponseEntity<List<PostDto>> getPosts() {
        return ResponseEntity.ok(this.postService.fetchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable("id") Long id) {
        PostDto postFetch = this.postService.fetchById(id)
                .orElseThrow(() -> this.resoruceNotFound(id));
        return ResponseEntity.ok(postFetch);
    }

    @GetMapping("/{id}/comment")
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable("id") Long id) {
        List<CommentDto> comments = commentService.fetchAllByPost(id);
        return ResponseEntity.ok(comments);
    }


    @PostMapping("/")
    public ResponseEntity<PostDto> postPost(@Validated @RequestBody PostDto post, Errors errors) {
        this.errorHandler(errors);
        if (post.getId() != null) {
            throw new BadRequestAlertException("El recurso tiene un id asignado", this.klass.getName(), "ASSIGNED_ID");
        }
        PostDto newPost = this.postService.create(post);
        URI uriNewEntity = this.generateNewUri(newPost.getId());
        return ResponseEntity.created(uriNewEntity).body(newPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> update(@PathVariable("id") Long id, @RequestBody PostDto postDto) {
        PostDto updateDto = this.postService.update(id, postDto);
        return ResponseEntity.ok(updateDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id) {
        this.postService.remove(id);
        return ResponseEntity.noContent()
                .header("accepted", "delete id " + id)
                .build();
    }

}

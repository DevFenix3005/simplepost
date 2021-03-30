package com.rebirth.simplepost.web.controllers;


import com.rebirth.simplepost.domain.tables.dtos.PostDto;
import com.rebirth.simplepost.domain.tables.dtos.PostWithTagDto;
import com.rebirth.simplepost.services.PostService;
import com.rebirth.simplepost.services.dtos.MixPostTags;
import com.rebirth.simplepost.services.dtos.PostWithTags;
import com.rebirth.simplepost.web.errors.BadRequestAlertException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController extends BaseController<PostDto, Long> {

    private final PostService postService;

    public PostController(PostService postService) {
        super(PostDto.class);
        this.postService = postService;
    }

    @GetMapping("/")
    public ResponseEntity<List<PostWithTagDto>> getPosts(@RequestParam(required = false, name = "__f", defaultValue = "") String filtro) {
        return ResponseEntity.ok(this.postService.findAllPostWithTags(filtro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable("id") Long id) {
        PostDto postFetch = this.postService.fetchById(id)
                .orElseThrow(() -> this.resoruceNotFound(id));
        return ResponseEntity.ok(postFetch);
    }

    @GetMapping("/mix")
    public ResponseEntity<List<MixPostTags>> getMixPost(@RequestParam(required = false, name = "__f", defaultValue = "") String filtro) {
        List<MixPostTags> fetchPostMixing = this.postService.fetchPostMixing(filtro);
        return ResponseEntity.ok(fetchPostMixing);
    }


    @PostMapping("/mix")
    public ResponseEntity<MixPostTags> postPost(@Validated @RequestBody MixPostTags post, Errors errors) {
        this.errorHandler(errors);
        PostDto postDto = post.getPost();
        if (postDto.getPostId() != null) {
            throw new BadRequestAlertException("El recurso tiene un id asignado", this.klass.getName(), "ASSIGNED_ID");
        }
        MixPostTags newPost = this.postService.createPostMixing(post);
        URI uriNewEntity = this.generateNewUri(newPost.getPost().getPostId());
        return ResponseEntity.created(uriNewEntity).body(newPost);
    }


    @PostMapping("/")
    public ResponseEntity<PostWithTags> postPost(@Validated @RequestBody PostWithTags post, Errors errors) {
        this.errorHandler(errors);
        if (post.getPostId() != null) {
            throw new BadRequestAlertException("El recurso tiene un id asignado", this.klass.getName(), "ASSIGNED_ID");
        }
        PostWithTags newPost = this.postService.create(post);
        URI uriNewEntity = this.generateNewUri(newPost.getPostId());
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

package com.rebirth.simplepost.web.controllers;

import com.rebirth.simplepost.domain.entities.Post;
import com.rebirth.simplepost.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController extends BaseController<Post, Long> {

    private final PostService postService;

    public PostController(PostService postService) {
        super(Post.class);
        this.postService = postService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Post>> getPosts() {
        return ResponseEntity.ok(this.postService.fetchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable("id") Long id) {
        Post comment = this.postService.fetchById(id)
                .orElseThrow(() -> this.resoruceNotFound(id));
        return ResponseEntity.ok(comment);
    }


    @PostMapping("/")
    public ResponseEntity<Post> postPost(@RequestBody Post post) {
        Post newPost = this.postService.create(post);
        URI uriNewEntity = this.generateNewUri(newPost.getId());
        return ResponseEntity.created(uriNewEntity).body(newPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id) {
        this.postService.remove(id);
        return ResponseEntity.noContent()
                .header("accepted", "delete id " + id)
                .build();
    }

}

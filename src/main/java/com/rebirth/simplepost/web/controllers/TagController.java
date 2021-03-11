package com.rebirth.simplepost.web.controllers;

import com.rebirth.simplepost.domain.entities.Tag;
import com.rebirth.simplepost.services.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController extends BaseController<Tag, Long> {

    private final TagService tagService;

    public TagController(TagService tagService) {
        super(Tag.class);
        this.tagService = tagService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Tag>> getTags() {
        return ResponseEntity.ok(this.tagService.fetchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTag(@PathVariable("id") Long id) {
        Tag tag = this.tagService.fetchById(id)
                .orElseThrow(() -> this.resoruceNotFound(id));
        return ResponseEntity.ok(tag);
    }


    @PostMapping("/")
    public ResponseEntity<Tag> postTag(@RequestBody Tag tag) {
        Tag newPost = this.tagService.create(tag);
        URI uriNewEntity = this.generateNewUri(newPost.getId());
        return ResponseEntity.created(uriNewEntity).body(newPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable("id") Long id) {
        this.tagService.remove(id);
        return ResponseEntity.noContent()
                .header("accepted", "delete id " + id)
                .build();
    }
}
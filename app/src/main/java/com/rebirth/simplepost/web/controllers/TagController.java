package com.rebirth.simplepost.web.controllers;

import com.rebirth.simplepost.domain.tables.dtos.TagDto;
import com.rebirth.simplepost.services.TagService;
import com.rebirth.simplepost.services.dtos.TagWithPosts;
import com.rebirth.simplepost.web.errors.BadRequestAlertException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController extends BaseController<TagDto, Long> {

    private final TagService tagService;

    public TagController(TagService tagService) {
        super(TagDto.class);
        this.tagService = tagService;
    }

    @GetMapping("/")
    public ResponseEntity<List<TagDto>> getTags(@RequestParam(name = "__f", required = false, defaultValue = "") String filter) {
        return ResponseEntity.ok(this.tagService.fetchAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getTag(@PathVariable("id") Long id) {
        TagDto tag = this.tagService.fetchById(id)
                .orElseThrow(() -> this.resoruceNotFound(id));
        return ResponseEntity.ok(tag);
    }


    @GetMapping("/{id}/posts")
    public ResponseEntity<TagWithPosts> getPostsByTag(@PathVariable("id") Long id) {
        TagWithPosts posts = this.tagService.getPostByTag(id);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/")
    public ResponseEntity<TagDto> postTag(@Validated @RequestBody TagDto tag, Errors errors) {
        this.errorHandler(errors);
        if (tag.getTagId() != null) {
            throw new BadRequestAlertException("El recurso tiene un id asignado", this.klass.getName(), "ASSIGNED_ID");
        }
        TagDto newPost = this.tagService.create(tag);
        URI uriNewEntity = this.generateNewUri(newPost.getTagId());
        return ResponseEntity.created(uriNewEntity).body(newPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDto> update(@PathVariable("id") Long id, @RequestBody TagDto tagDto) {
        TagDto updateDto = this.tagService.update(id, tagDto);
        return ResponseEntity.ok(updateDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable("id") Long id) {
        this.tagService.remove(id);
        return ResponseEntity.noContent()
                .header("accepted", "delete id " + id)
                .build();
    }
}
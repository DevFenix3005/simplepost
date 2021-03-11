package com.rebirth.simplepost.services.impl;

import com.rebirth.simplepost.domain.entities.Post;
import com.rebirth.simplepost.domain.entities.Tag;
import com.rebirth.simplepost.domain.repositories.PostRepository;
import com.rebirth.simplepost.domain.repositories.TagRepository;
import com.rebirth.simplepost.services.PostService;
import com.rebirth.simplepost.services.dtos.PostDto;
import com.rebirth.simplepost.web.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PostServiceImpl
        extends AbstractBaseService<Post, PostDto, Long, PostRepository>
        implements PostService {

    private final TagRepository tagRepository;

    @Autowired
    public PostServiceImpl(PostRepository repository, TagRepository tagRepository) {
        super(Post.class, PostDto.class, repository);
        this.tagRepository = tagRepository;
    }

    @Override
    public PostDto create(PostDto entity) {
        Set<String> tags = entity.getTags();

        Post e2daoPost = new Post();
        e2daoPost.setTitle(entity.getTitle());
        e2daoPost.setBody(entity.getBody());

        for (String tag : tags) {
            if (tagRepository.existsByName(tag)) {
                Tag newTag = tagRepository.findByName(tag);
                e2daoPost.addTag(newTag);
            } else {
                e2daoPost.addTag(new Tag(tag));
            }
        }

        Post newPost = this.repository.saveAndFlush(e2daoPost);
        return this.dao2dto(newPost);
    }

    @Override
    public PostDto update(Long id, PostDto entity) {
        if (id.equals(entity.getId())) {
            Post result = this.repository.findById(id)
                    .orElseThrow(() -> new BadRequestAlertException("No se encontro el recurso", PostDto.class.getName(), "NOT_FOUND"));
            result.getTags().clear();

            String title = entity.getTitle();
            if (title != null) {
                result.setTitle(title);
            }

            String body = entity.getBody();
            result.setBody(body);

            Set<String> rawTags = entity.getTags();

            for (String tag : rawTags) {
                if (tagRepository.existsByName(tag)) {
                    Tag newTag = tagRepository.findByName(tag);
                    result.addTag(newTag);
                } else {
                    result.addTag(new Tag(tag));
                }
            }
            Post updateResource = this.repository.save(result);
            return this.dao2dto(updateResource);
        } else {
            throw new BadRequestAlertException("No se puede actualizar, ya que los ids no coinciden", entity.getClass().getName(), "Update fail");
        }


    }
}

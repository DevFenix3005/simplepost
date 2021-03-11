package com.rebirth.simplepost.services.impl;

import com.rebirth.simplepost.domain.entities.Tag;
import com.rebirth.simplepost.domain.repositories.TagRepository;
import com.rebirth.simplepost.services.TagService;
import com.rebirth.simplepost.services.dtos.PostDto;
import com.rebirth.simplepost.services.dtos.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl extends AbstractBaseService<Tag, TagDto, Long, TagRepository> implements TagService {

    @Autowired
    public TagServiceImpl(TagRepository repository) {
        super(Tag.class, TagDto.class, repository);
    }

    @Override
    public List<PostDto> getPostByTag(Long tagId) {
        return repository.getPostByTag(tagId)
                .stream()
                .map(post -> {
                    return this.dozerMapper.map(post, PostDto.class);
                })
                .collect(Collectors.toList());
    }
}

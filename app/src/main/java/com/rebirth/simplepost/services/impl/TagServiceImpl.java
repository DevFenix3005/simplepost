package com.rebirth.simplepost.services.impl;

import com.rebirth.simplepost.components.FilterComponent;
import com.rebirth.simplepost.domain.tables.dtos.PostDto;
import com.rebirth.simplepost.domain.tables.dtos.PostsTagDto;
import com.rebirth.simplepost.domain.tables.dtos.TagDto;
import com.rebirth.simplepost.domain.tables.records.TagsRecord;
import com.rebirth.simplepost.domain.tables.repositories.PostRepository;
import com.rebirth.simplepost.domain.tables.repositories.PostsTagRepository;
import com.rebirth.simplepost.domain.tables.repositories.TagRepository;
import com.rebirth.simplepost.services.AbstractService;
import com.rebirth.simplepost.services.TagService;
import com.rebirth.simplepost.services.dtos.TagWithPosts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TagServiceImpl
        extends AbstractService<TagsRecord, TagDto, Long, TagRepository>
        implements TagService {

    private final PostRepository postRepository;
    private final PostsTagRepository postsTagRepository;

    public TagServiceImpl(TagRepository repository,
                          PostRepository postRepository,
                          PostsTagRepository postsTagRepository,
                          FilterComponent filterComponent) {
        super("tagId", repository, filterComponent);
        this.postRepository = postRepository;
        this.postsTagRepository = postsTagRepository;
    }

    @Override
    public TagWithPosts getPostByTag(Long tagId) {
        TagDto tag = this.fetchById(tagId).get();
        log.info("TAG -> {}", tag.toString());
        List<PostsTagDto> postsTagDtoList = postsTagRepository.fetchByTagId(tagId);
        Long[] postIds = new Long[postsTagDtoList.size()];
        for (int i = 0; i < postsTagDtoList.size(); i++) {
            postIds[i] = postsTagDtoList.get(i).getPostId();
        }
        List<PostDto> posts = this.postRepository.fetchByPostId(postIds);
        return new TagWithPosts(tag, posts);
    }

    @Override
    public List<TagDto> fetch2UpdateMovil() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after4Hours = now.minusHours(4L);
        return this.repository.fetchRangeOfUpdateAt(after4Hours, now);
    }


}

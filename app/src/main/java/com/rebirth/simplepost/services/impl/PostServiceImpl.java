package com.rebirth.simplepost.services.impl;

import com.rebirth.simplepost.components.FilterComponent;
import com.rebirth.simplepost.domain.tables.PostWithTag;
import com.rebirth.simplepost.domain.tables.dtos.*;
import com.rebirth.simplepost.domain.tables.records.PostWithTagsRecord;
import com.rebirth.simplepost.domain.tables.records.PostsRecord;
import com.rebirth.simplepost.domain.tables.repositories.CommentRepository;
import com.rebirth.simplepost.domain.tables.repositories.PostRepository;
import com.rebirth.simplepost.domain.tables.repositories.PostsTagRepository;
import com.rebirth.simplepost.domain.tables.repositories.TagRepository;
import com.rebirth.simplepost.services.AbstractService;
import com.rebirth.simplepost.services.PostService;
import com.rebirth.simplepost.services.dtos.PostWithComments;
import com.rebirth.simplepost.services.dtos.PostWithTags;
import com.rebirth.simplepost.utils.filters.QryFilter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.SelectWhereStep;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.upper;

@Service
@Slf4j
public class PostServiceImpl extends AbstractService<PostsRecord, PostDto, Long, PostRepository> implements PostService {

    private final DefaultDSLContext dslContext;
    private final TagRepository tagRepository;
    private final PostsTagRepository postsTagRepository;
    private final CommentRepository commentRepository;

    public PostServiceImpl(PostRepository repository,
                           DefaultDSLContext dslContext,
                           TagRepository tagRepository,
                           PostsTagRepository postsTagRepository,
                           CommentRepository commentRepository,
                           FilterComponent filterComponent) {
        super("postId", repository, filterComponent);
        this.dslContext = dslContext;
        this.tagRepository = tagRepository;
        this.postsTagRepository = postsTagRepository;
        this.commentRepository = commentRepository;
    }

    public List<PostWithTagDto> findAllPostWithTags(String filter) {
        PostWithTag postWithTag = PostWithTag.POST_WITH_TAGS.as("pwt0");
        SelectWhereStep<PostWithTagsRecord> selectFrom = dslContext.selectFrom(postWithTag);
        if (!filter.isBlank()) {
            Map<String, QryFilter> filtros = filterComponent.query2Filtro(filter);
            Condition[] conditions = new Condition[filtros.size()];
            int c = 0;
            if (filtros.containsKey("title")) {
                QryFilter qryFilterTitle = filtros.get("title");
                conditions[c++] = upper(postWithTag.TITLE).like((String) qryFilterTitle.getValue());
                log.info("Filtro Title: {}", qryFilterTitle.toString());
            }
            if (filtros.containsKey("tags")) {
                QryFilter qryFilterTags = filtros.get("tags");
                conditions[c] = condition("tags && ?", qryFilterTags.getValue());
                log.info("Filtro Tags: {}", qryFilterTags.toString());
            }
            if (!filtros.isEmpty()) {
                selectFrom.where(conditions);
            }
        }
        return selectFrom.fetchInto(PostWithTagDto.class);
    }


    @Override
    public PostWithTags create(PostWithTags post) {
        this.create((PostDto) post);
        List<String> tags = post.getTags();
        if (tags != null) {
            for (String tag : tags) {
                TagDto tagDto = this.tagRepository.fetchOneByName(tag);
                if (tagDto == null) {
                    tagDto = new TagDto();
                    tagDto.setName(tag);
                    this.tagRepository.insert(tagDto);
                }
                PostsTagDto postsTagDto = new PostsTagDto(post.getPostId(), tagDto.getTagId());
                this.postsTagRepository.insert(postsTagDto);
            }
        }

        return post;
    }

    @Override
    public PostWithComments fetchPostWithComments(Long id) {
        PostDto post = this.fetchById(id).get();
        List<CommentDto> comments = commentRepository.fetchByPostId(id);
        return new PostWithComments(post, comments);
    }

    @Override
    public List<PostDto> fetch2UpdateMovil() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after4Hours = now.minusHours(4L);
        return this.repository.fetchRangeOfUpdateAt(after4Hours, now);
    }
}

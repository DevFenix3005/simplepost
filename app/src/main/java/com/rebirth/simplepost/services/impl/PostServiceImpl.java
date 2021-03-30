package com.rebirth.simplepost.services.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pusher.rest.Pusher;
import com.pusher.rest.data.Result;
import com.rebirth.simplepost.components.FilterComponent;
import com.rebirth.simplepost.domain.tables.Post;
import com.rebirth.simplepost.domain.tables.PostWithTag;
import com.rebirth.simplepost.domain.tables.PostsTag;
import com.rebirth.simplepost.domain.tables.Tag;
import com.rebirth.simplepost.domain.tables.dtos.*;
import com.rebirth.simplepost.domain.tables.records.PostWithTagsRecord;
import com.rebirth.simplepost.domain.tables.records.PostsRecord;
import com.rebirth.simplepost.domain.tables.repositories.CommentRepository;
import com.rebirth.simplepost.domain.tables.repositories.PostRepository;
import com.rebirth.simplepost.domain.tables.repositories.PostsTagRepository;
import com.rebirth.simplepost.domain.tables.repositories.TagRepository;
import com.rebirth.simplepost.services.AbstractService;
import com.rebirth.simplepost.services.PostService;
import com.rebirth.simplepost.services.dtos.MixPostTags;
import com.rebirth.simplepost.services.dtos.PostWithComments;
import com.rebirth.simplepost.services.dtos.PostWithTags;
import com.rebirth.simplepost.services.dtos.PusherMessage;
import com.rebirth.simplepost.utils.filters.QryFilter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.SelectWhereStep;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
    private final Pusher pusher;

    public PostServiceImpl(PostRepository repository,
                           DefaultDSLContext dslContext,
                           TagRepository tagRepository,
                           PostsTagRepository postsTagRepository,
                           CommentRepository commentRepository,
                           FilterComponent filterComponent,
                           Pusher pusher) {
        super("postId", repository, filterComponent);
        this.dslContext = dslContext;
        this.tagRepository = tagRepository;
        this.postsTagRepository = postsTagRepository;
        this.commentRepository = commentRepository;
        this.pusher = pusher;
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
    public MixPostTags createPostMixing(MixPostTags postTags) {

        PostDto postDto = postTags.getPost();

        this.repository.insert(postDto);
        MixPostTags resultMixPostTags;

        List<TagDto> tags = postTags.getTags();
        if (tags != null) {
            List<TagDto> newsTags = Lists.newArrayList();
            for (TagDto tag : tags) {
                String name = tag.getName();
                TagDto tagDto = this.tagRepository.fetchOneByName(name);
                if (tagDto == null) {
                    tagDto = new TagDto().setName(name);
                    this.tagRepository.insert(tagDto);
                }
                this.postsTagRepository.insert(new PostsTagDto(postDto.getPostId(), tagDto.getTagId()));
                newsTags.add(tagDto);
            }
            resultMixPostTags = new MixPostTags(postDto, newsTags);
        } else {
            resultMixPostTags = new MixPostTags(postDto, Collections.emptyList());
        }
        PusherMessage<MixPostTags> commentDtoPusherMessage = new PusherMessage<>();
        commentDtoPusherMessage.setMensaje("Se a agregado un nuevo post");
        commentDtoPusherMessage.setBody(resultMixPostTags);
        pusher.trigger("my-channel", "new-post-event", commentDtoPusherMessage);
        return resultMixPostTags;
    }

    @Override
    public List<MixPostTags> fetchPostMixing(String filter) {
        Post post = Post.POSTS.as("p0");
        PostsTag postsTag = PostsTag.POSTS_TAGS.as("pt0");
        Tag tag = Tag.TAGS.as("t0");

        SelectJoinStep<Record> query = dslContext.select(post.asterisk(), tag.asterisk())
                .from(post).leftJoin(postsTag).using(postsTag.POST_ID)
                .innerJoin(tag).using(postsTag.TAG_ID);

        if (!filter.isBlank()) {
            Map<String, QryFilter> filtros = filterComponent.query2Filtro(filter);
            Condition[] conditions = new Condition[filtros.size()];
            int c = 0;
            if (filtros.containsKey("title")) {
                QryFilter qryFilterTitle = filtros.get("title");
                conditions[c++] = upper(post.TITLE).like((String) qryFilterTitle.getValue());
                log.info("Filtro Title: {}", qryFilterTitle.toString());
            }
            if (filtros.containsKey("tags")) {
                QryFilter qryFilterTags = filtros.get("tags");
                String[] tagsFilterValue = (String[]) qryFilterTags.getValue();
                conditions[c] = tag.NAME.in(tagsFilterValue);
                log.info("Filtro Tags: {}", qryFilterTags.toString());
            }
            if (!filtros.isEmpty()) {
                query.where(conditions);
            }
        }
        query.orderBy(post.CREATE_AT);
        log.info(query.getSQL());
        Map<Long, MixPostTags> mixPostTagsMap = Maps.newHashMap();

        for (Record fetch : query.fetch()) {
            Long postId = fetch.get(post.POST_ID);
            LocalDateTime createAtPost = fetch.get(post.CREATE_AT);
            String createByPost = fetch.get(post.CREATE_BY);
            LocalDateTime updateAtPost = fetch.get(post.UPDATE_AT);
            String updateByPost = fetch.get(post.UPDATE_BY);
            Long versionPost = fetch.get(post.VERSION);
            String titlePost = fetch.get(post.TITLE);
            String bodyPost = fetch.get(post.BODY);

            Long tagId = fetch.get(tag.TAG_ID);
            LocalDateTime createAtTag = fetch.get(tag.CREATE_AT);
            String createByTag = fetch.get(tag.CREATE_BY);
            LocalDateTime updateAtTag = fetch.get(tag.UPDATE_AT);
            String updateByTag = fetch.get(tag.UPDATE_BY);
            Long versionTag = fetch.get(tag.VERSION);
            String nameTag = fetch.get(tag.NAME);

            TagDto tagDto = new TagDto(tagId, createAtTag, createByTag, updateAtTag, updateByTag, versionTag, nameTag);
            MixPostTags current;
            if (mixPostTagsMap.containsKey(postId)) {
                current = mixPostTagsMap.get(postId);
            } else {
                current = new MixPostTags();
                current.setPost(new PostDto(postId, createAtPost, createByPost, updateAtPost, updateByPost, versionPost, bodyPost, titlePost));
                mixPostTagsMap.put(postId, current);
            }
            current.addTag(tagDto);
        }
        return new ArrayList<>(mixPostTagsMap.values());
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

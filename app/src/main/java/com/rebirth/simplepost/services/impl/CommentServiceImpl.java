package com.rebirth.simplepost.services.impl;

import com.pusher.rest.Pusher;
import com.rebirth.simplepost.components.FilterComponent;
import com.rebirth.simplepost.domain.tables.Comment;
import com.rebirth.simplepost.domain.tables.dtos.CommentDto;
import com.rebirth.simplepost.domain.tables.records.CommentsRecord;
import com.rebirth.simplepost.domain.tables.repositories.CommentRepository;
import com.rebirth.simplepost.services.AbstractService;
import com.rebirth.simplepost.services.CommentService;
import com.rebirth.simplepost.services.dtos.PusherMessage;
import com.rebirth.simplepost.utils.filters.QryFilter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.SelectWhereStep;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CommentServiceImpl extends AbstractService<CommentsRecord, CommentDto, Long, CommentRepository> implements CommentService {

    private final DefaultDSLContext dslContext;
    private final Pusher pusher;

    public CommentServiceImpl(CommentRepository repository,
                              FilterComponent filterComponent,
                              DefaultDSLContext dslContext,
                              Pusher pusher) {
        super("commentId", repository, filterComponent);
        this.dslContext = dslContext;
        this.pusher = pusher;
    }

    @Override
    public List<CommentDto> fetchAll(String filter) {
        Comment comment = Comment.COMMENTS.as("c0");
        SelectWhereStep<CommentsRecord> selectFrom = dslContext.selectFrom(comment);
        if (!filter.isBlank()) {
            Map<String, QryFilter> filtros = filterComponent.query2Filtro(filter);
            Condition[] conditions = new Condition[filtros.size()];
            int c = 0;
            if (filtros.containsKey("postId")) {
                QryFilter qryFilterTitle = filtros.get("postId");
                Long postId = Long.parseLong(qryFilterTitle.getValue().toString());
                conditions[c++] = comment.POST_ID.eq(postId);
                log.info("Filtro PostID: {}", qryFilterTitle.toString());
            }
            if (!filtros.isEmpty()) {
                selectFrom.where(conditions);
            }
            log.info(selectFrom.toString());
            return selectFrom.fetchInto(CommentDto.class);
        } else return this.fetchAll();
    }

    @Override
    public List<CommentDto> fetchAllByPost(Long id) {
        return this.repository.fetchByPostId(id);
    }

    @Override
    public List<CommentDto> fetch2UpdateMovil() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after4Hours = now.minusHours(4L);
        return this.repository.fetchRangeOfUpdateAt(after4Hours, now);
    }


    @Override
    public CommentDto create(CommentDto entity) {
        CommentDto commentDto = super.create(entity);
        PusherMessage<CommentDto> commentDtoPusherMessage = new PusherMessage<>();
        commentDtoPusherMessage.setMensaje("Se a agregado un nuevo comentario");
        commentDtoPusherMessage.setBody(commentDto);
        pusher.trigger("my-channel", "new-comment-event", commentDtoPusherMessage);
        return commentDto;
    }
}

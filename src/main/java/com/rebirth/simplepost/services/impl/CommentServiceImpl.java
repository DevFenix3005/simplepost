package com.rebirth.simplepost.services.impl;

import com.rebirth.simplepost.domain.entities.Comment;
import com.rebirth.simplepost.domain.repositories.CommentRepository;
import com.rebirth.simplepost.services.CommentService;
import com.rebirth.simplepost.services.dtos.CommentDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl
        extends AbstractBaseService<Comment, CommentDto, Long, CommentRepository>
        implements CommentService {

    public CommentServiceImpl(CommentRepository repository) {
        super(Comment.class, CommentDto.class, repository);
    }

    @Override
    public CommentDto create(CommentDto entity) {
        Long postId = entity.getPostId();
        String body = entity.getBody();

        Comment newComment = new Comment();
        newComment.setPostId(postId);
        newComment.setBody(body);
        newComment = repository.save(newComment);

        return this.dao2dto(newComment);
    }

    @Override
    public List<CommentDto> fetchAllByPost(Long id) {
        return this.repository.findByPostId(id)
                .stream()
                .map(this::dao2dto)
                .collect(Collectors.toList());
    }
}

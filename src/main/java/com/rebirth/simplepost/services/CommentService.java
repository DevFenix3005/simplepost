package com.rebirth.simplepost.services;

import com.rebirth.simplepost.domain.entities.Comment;
import com.rebirth.simplepost.services.dtos.CommentDto;

import java.util.List;

public interface CommentService extends BaseService<Comment, CommentDto, Long> {
    List<CommentDto> fetchAllByPost(Long id);
}


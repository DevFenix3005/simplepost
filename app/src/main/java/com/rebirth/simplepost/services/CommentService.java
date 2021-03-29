package com.rebirth.simplepost.services;


import com.rebirth.simplepost.domain.tables.dtos.CommentDto;

import java.util.List;

public interface CommentService extends BaseService<CommentDto, Long> {
    List<CommentDto> fetchAllByPost(Long id);
}


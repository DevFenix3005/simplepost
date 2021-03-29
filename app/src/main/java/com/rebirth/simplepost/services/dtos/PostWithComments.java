package com.rebirth.simplepost.services.dtos;

import com.rebirth.simplepost.domain.tables.dtos.CommentDto;
import com.rebirth.simplepost.domain.tables.dtos.PostDto;
import lombok.Data;

import java.util.List;

@Data
public class PostWithComments {

    private final PostDto post;
    private final List<CommentDto> comments;

}

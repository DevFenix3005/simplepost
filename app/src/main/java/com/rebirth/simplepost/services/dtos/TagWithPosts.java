package com.rebirth.simplepost.services.dtos;

import com.rebirth.simplepost.domain.tables.dtos.PostDto;
import com.rebirth.simplepost.domain.tables.dtos.TagDto;
import lombok.Data;

import java.util.List;

@Data
public class TagWithPosts {

    private final TagDto tag;
    private final List<PostDto> posts;


}

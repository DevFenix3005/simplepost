package com.rebirth.simplepost.services;

import com.rebirth.simplepost.domain.entities.Tag;
import com.rebirth.simplepost.services.dtos.PostDto;
import com.rebirth.simplepost.services.dtos.TagDto;

import java.util.List;

public interface TagService extends BaseService<Tag, TagDto, Long> {

    List<PostDto> getPostByTag(Long tagId);

}

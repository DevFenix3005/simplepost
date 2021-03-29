package com.rebirth.simplepost.services;


import com.rebirth.simplepost.domain.tables.dtos.TagDto;
import com.rebirth.simplepost.services.dtos.TagWithPosts;

public interface TagService extends BaseService<TagDto, Long> {

    TagWithPosts getPostByTag(Long tagId);

}

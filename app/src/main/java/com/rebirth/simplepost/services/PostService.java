package com.rebirth.simplepost.services;

import com.rebirth.simplepost.domain.tables.dtos.PostDto;
import com.rebirth.simplepost.domain.tables.dtos.PostWithTagDto;
import com.rebirth.simplepost.services.dtos.MixPostTags;
import com.rebirth.simplepost.services.dtos.PostWithComments;
import com.rebirth.simplepost.services.dtos.PostWithTags;

import java.util.List;

public interface PostService extends BaseService<PostDto, Long> {

    PostWithTags create(PostWithTags post);

    PostWithComments fetchPostWithComments(Long id);

    List<PostWithTagDto> findAllPostWithTags(String filter);


    MixPostTags createPostMixing(MixPostTags postTags);

    List<MixPostTags> fetchPostMixing(String filter);
}

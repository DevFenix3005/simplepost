package com.rebirth.simplepost.services.impl;

import com.rebirth.simplepost.domain.entities.Post;
import com.rebirth.simplepost.domain.repositories.PostRepository;
import com.rebirth.simplepost.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl
        extends AbstractBaseService<Post, Long, PostRepository>
        implements PostService {

    @Autowired
    public PostServiceImpl(PostRepository repository) {
        super(repository);
    }
}

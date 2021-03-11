package com.rebirth.simplepost.services.impl;

import com.rebirth.simplepost.domain.entities.Comment;
import com.rebirth.simplepost.domain.repositories.CommentRepository;
import com.rebirth.simplepost.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl
        extends AbstractBaseService<Comment, Long, CommentRepository>
        implements CommentService {

    @Autowired
    public CommentServiceImpl(CommentRepository repository) {
        super(repository);
    }
}

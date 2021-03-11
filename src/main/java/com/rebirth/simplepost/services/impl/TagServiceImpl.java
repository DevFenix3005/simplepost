package com.rebirth.simplepost.services.impl;

import com.rebirth.simplepost.domain.entities.Tag;
import com.rebirth.simplepost.domain.repositories.TagRepository;
import com.rebirth.simplepost.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl extends AbstractBaseService<Tag, Long, TagRepository> implements TagService {

    @Autowired
    public TagServiceImpl(TagRepository repository) {
        super(repository);
    }
}

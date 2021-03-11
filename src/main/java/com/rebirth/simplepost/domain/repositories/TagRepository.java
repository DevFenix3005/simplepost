package com.rebirth.simplepost.domain.repositories;

import com.rebirth.simplepost.domain.entities.Post;
import com.rebirth.simplepost.domain.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(collectionResourceRel = "tag", path = "tag")
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByName(@Param("name") String name);


}



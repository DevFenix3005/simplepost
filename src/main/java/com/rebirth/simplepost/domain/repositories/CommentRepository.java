package com.rebirth.simplepost.domain.repositories;

import com.rebirth.simplepost.domain.entities.Comment;
import com.rebirth.simplepost.domain.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(collectionResourceRel = "comment", path = "comment")
public interface CommentRepository extends JpaRepository<Comment, Long> {


}



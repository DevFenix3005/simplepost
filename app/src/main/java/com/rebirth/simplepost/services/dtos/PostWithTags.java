package com.rebirth.simplepost.services.dtos;

import com.google.common.base.MoreObjects;
import com.rebirth.simplepost.domain.tables.dtos.PostDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostWithTags extends PostDto {

    private List<String> tags;


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("tags", tags)
                .toString();
    }
}

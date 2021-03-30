package com.rebirth.simplepost.services.dtos;

import com.rebirth.simplepost.domain.tables.dtos.PostDto;
import com.rebirth.simplepost.domain.tables.dtos.TagDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MixPostTags implements Serializable {

    @Valid
    private PostDto post;

    @Valid
    private List<TagDto> tags;


    public void addTag(TagDto tagDto) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.add(tagDto);
    }

}

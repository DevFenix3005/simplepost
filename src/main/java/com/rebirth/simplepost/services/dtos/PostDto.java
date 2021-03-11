package com.rebirth.simplepost.services.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostDto extends BaseDto {

    @NotNull
    @NotEmpty
    @NotBlank
    @Length(min = 1, max = 150)
    private String title;

    @NotEmpty
    @NotBlank
    @Length(min = 1, max = 300)
    private String body;

    private List<CommentDto> comments = new ArrayList<>();

    private Set<String> tags = new HashSet<>();

}

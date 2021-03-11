package com.rebirth.simplepost.services.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentDto extends BaseDto {

    @NotNull
    @NotEmpty
    @NotBlank
    @Length(min = 1, max = 300)
    private String body;

    @JsonProperty("post_id")
    private Long postId;

}

package com.rebirth.simplepost.services.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TagDto extends BaseDto {

    @NotNull
    @NotEmpty
    @NotBlank
    @Length(min = 1, max = 30)
    private String name;

    private List<PostDto> posts = new ArrayList<>();

}

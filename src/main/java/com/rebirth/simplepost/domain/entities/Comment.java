package com.rebirth.simplepost.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "comments")
@NoArgsConstructor
@RequiredArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "comment_id"))
@EqualsAndHashCode(callSuper = true)
public class Comment extends Auditor<Long> {

    @NotNull
    @NonNull
    @Column(columnDefinition = "text")
    @Length(min = 1, max = 300)
    @NotEmpty
    @NotBlank
    private String body;

}

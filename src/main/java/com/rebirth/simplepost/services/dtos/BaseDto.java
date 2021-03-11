package com.rebirth.simplepost.services.dtos;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseDto implements Serializable {

    private Long id;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String createBy;
}

package com.rebirth.simplepost.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Greeting {

    private Integer id;
    private String greet;

}

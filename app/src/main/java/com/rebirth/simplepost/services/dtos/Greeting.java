package com.rebirth.simplepost.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Greeting {
    private Long id;
    private String mensaje;
}

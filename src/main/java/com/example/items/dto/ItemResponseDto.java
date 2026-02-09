package com.example.items.dto;

import lombok.Data;

@Data
public class ItemResponseDto {

    private Long id;
    private String name;
    private Integer extraNumber;
    private String extraText;

}

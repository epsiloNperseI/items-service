package com.example.items.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemRequestDto {

    @NotBlank
    private String name;

    @Min(0)
    private Integer extraNumber;

    @Size(max = 255)
    private String extraText;

}

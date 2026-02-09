package com.example.items.dto;

import java.util.List;
import lombok.Data;

@Data
public class ExportRequestDto {

    private List<String> columns;
}

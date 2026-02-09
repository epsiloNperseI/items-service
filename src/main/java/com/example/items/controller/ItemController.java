package com.example.items.controller;

import com.example.items.dto.ExportRequestDto;
import com.example.items.dto.ItemRequestDto;
import com.example.items.dto.ItemResponseDto;
import com.example.items.service.ItemExportService;
import com.example.items.service.ItemService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService service;
    private final ItemExportService exportService;

    @PostMapping
    public ItemResponseDto create(@Valid @RequestBody ItemRequestDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ItemResponseDto update(
        @PathVariable Long id,
        @Valid @RequestBody ItemRequestDto dto
    ) {
        return service.update(id, dto);
    }

    @GetMapping
    public List<ItemResponseDto> getAll() {
        return service.findAll();
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> export(@RequestBody ExportRequestDto request) {
        return exportService.export(request);
    }

}

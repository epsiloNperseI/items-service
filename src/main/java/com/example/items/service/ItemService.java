package com.example.items.service;

import com.example.items.dto.ItemRequestDto;
import com.example.items.dto.ItemResponseDto;
import com.example.items.entity.Item;
import com.example.items.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;

    @Transactional
    public ItemResponseDto create(ItemRequestDto dto) {
        Item item = new Item();
        // TODO: маппинг нужно вынести в спринг мапперы
        item.setName(dto.getName());
        item.setExtraNumber(dto.getExtraNumber());
        item.setExtraText(dto.getExtraText());

        return map(repository.save(item));
    }

    @Transactional
    public ItemResponseDto update(Long id, ItemRequestDto dto) {
        Item item = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setName(dto.getName());
        item.setExtraNumber(dto.getExtraNumber());
        item.setExtraText(dto.getExtraText());

        return map(repository.save(item));
    }

    @Transactional(readOnly = true)
    public List<ItemResponseDto> findAll() {
        return repository.findAll().stream()
            .map(this::map)
            .toList();
    }

    private ItemResponseDto map(Item item) {
        ItemResponseDto dto = new ItemResponseDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setExtraNumber(item.getExtraNumber());
        dto.setExtraText(item.getExtraText());
        return dto;
    }
}

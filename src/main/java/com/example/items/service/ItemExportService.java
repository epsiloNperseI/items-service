package com.example.items.service;

import com.example.items.dto.ExportRequestDto;
import com.example.items.entity.Item;
import com.example.items.repository.ItemRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemExportService {

    private final ItemRepository repository;

    public ResponseEntity<byte[]> export(ExportRequestDto request) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("items");
            List<Item> items = repository.findAll();
            List<String> columns = request.getColumns();

            Row header = sheet.createRow(0);
            for (int i = 0; i < columns.size(); i++) {
                header.createCell(i).setCellValue(columns.get(i));
            }

            for (int i = 0; i < items.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Item item = items.get(i);

                for (int j = 0; j < columns.size(); j++) {
                    row.createCell(j).setCellValue(
                        getValue(item, columns.get(j))
                    );
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=items.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Export failed", e);
        }
    }

    private String getValue(Item item, String column) {
        return switch (column) {
            case "id" -> String.valueOf(item.getId());
            case "name" -> item.getName();
            case "extraNumber" -> String.valueOf(item.getExtraNumber());
            case "extraText" -> item.getExtraText();
            default -> "";
        };
    }
}

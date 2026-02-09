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
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ItemExportService {

    private final ItemRepository repository;

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EXTRA_NUMBER = "extraNumber";
    public static final String COLUMN_EXTRA_TEXT = "extraText";

    public static final List<String> AVAILABLE_COLUMNS = List.of(
        COLUMN_ID, COLUMN_NAME, COLUMN_EXTRA_NUMBER, COLUMN_EXTRA_TEXT
    );


    public ResponseEntity<byte[]> export(ExportRequestDto request) {
        List<String> columns = request.getColumns();
        List<Item> items = repository.findAll();
        request.getColumns().forEach(column -> {
            if (!AVAILABLE_COLUMNS.contains(column)) {
                throw new IllegalArgumentException("Invalid column: " + column);
            }
        });

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("items");

            createHeader(sheet, columns);
            fillData(sheet, items, columns);

            return createResponse(workbook);
        } catch (IOException e) {
            throw new ExportException("Failed to generate Excel file", e);
        }
    }

    private void createHeader(Sheet sheet, List<String> columns) {
        Row header = sheet.createRow(0);
        for (int i = 0; i < columns.size(); i++) {
            String displayName = StringUtils.capitalize(
                columns.get(i).replaceAll("([A-Z])", " $1").toLowerCase()
            );
            header.createCell(i).setCellValue(displayName);
        }
    }

    private void fillData(Sheet sheet, List<Item> items, List<String> columns) {
        for (int i = 0; i < items.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Item item = items.get(i);
            fillRow(row, item, columns);
        }
    }

    private void fillRow(Row row, Item item, List<String> columns) {
        for (int j = 0; j < columns.size(); j++) {
            row.createCell(j).setCellValue(getValue(item, columns.get(j)));
        }
    }

    private ResponseEntity<byte[]> createResponse(Workbook workbook) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"items.xlsx\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(out.toByteArray());
    }

    private String getValue(Item item, String column) {
        return switch (column) {
            case COLUMN_ID -> String.valueOf(item.getId());
            case COLUMN_NAME -> item.getName();
            case COLUMN_EXTRA_NUMBER -> item.getExtraNumber() != null
                ? String.valueOf(item.getExtraNumber()) : "";
            case COLUMN_EXTRA_TEXT -> item.getExtraText() != null ? item.getExtraText() : "";
            default -> throw new IllegalArgumentException("Unknown column: " + column);
        };
    }

    public static class ExportException extends RuntimeException {
        public ExportException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
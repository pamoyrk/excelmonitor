package excelmonitor;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ExcelMonitorTest {
    private static final Path TEST_FILE = Path.of("ScienceFair.xlsx");

    @AfterEach
    void cleanup() throws IOException {
        if (Files.exists(TEST_FILE)) {
            Files.delete(TEST_FILE);
        }
    }

    @Test
    void reportsMissingNameAndIncorrectAverage() throws Exception {
        createTestWorkbook();

        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            ExcelMonitor.main(new String[0]);
        } finally {
            System.setOut(originalOut);
        }

        String result = output.toString();
        assertTrue(result.contains("Missing student name or project title"));
        assertTrue(result.contains("Incorrect average score"));
    }

    private void createTestWorkbook() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Student");
            header.createCell(1).setCellValue("Project");
            header.createCell(2).setCellValue("Judge1");
            header.createCell(3).setCellValue("Judge2");
            header.createCell(4).setCellValue("Average");

            Row row = sheet.createRow(1);
            row.createCell(1).setCellValue("Project A");
            row.createCell(2).setCellValue(3);
            row.createCell(3).setCellValue(5);
            row.createCell(4).setCellValue(3); // incorrect average (should be 4)

            try (FileOutputStream out = new FileOutputStream(TEST_FILE.toFile())) {
                workbook.write(out);
            }
        }
    }
}

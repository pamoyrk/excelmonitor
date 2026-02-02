package excelmonitor;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class ExcelMonitorTest {
    private static final Path TEST_FILE = Path.of("ScienceFair.xlsx");

    @Test
    void fileExistsInDirectory() {
        assertTrue(Files.exists(TEST_FILE), "ScienceFair2.xlsx should exist in the project directory");
    }
}

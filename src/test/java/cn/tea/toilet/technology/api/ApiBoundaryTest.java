package cn.tea.toilet.technology.api;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiBoundaryTest {
    @Test
    void publicApiIsDocumentedAndDoesNotDependOnOptionalCompatibility() throws IOException {
        Path apiDirectory = Path.of("src/main/java/cn/tea/toilet/technology/api");
        String documentation = Files.readString(Path.of("docs/api/README.md"));

        assertTrue(Files.isDirectory(apiDirectory));
        assertTrue(documentation.contains("cn.tea.toilet.technology.api"));
        assertTrue(documentation.contains("GasAction.SIMULATE"));
        assertTrue(documentation.contains("ExternalGasHandlers"));

        try (var files = Files.walk(apiDirectory)) {
            assertFalse(files.filter(path -> path.toString().endsWith(".java"))
                    .map(ApiBoundaryTest::readUnchecked)
                    .anyMatch(source -> source.contains("mekanism.api") || source.contains("ExternalGasHandlers")));
        }
    }

    private static String readUnchecked(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }
}

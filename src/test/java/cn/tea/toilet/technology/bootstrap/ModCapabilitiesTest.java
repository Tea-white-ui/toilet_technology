package cn.tea.toilet.technology.bootstrap;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ModCapabilitiesTest {
    @Test
    void registersItemCapabilitiesForBiogasPondPorts() throws IOException {
        String source = Files.readString(Path.of("src/main/java/cn/tea/toilet/technology/bootstrap/ModCapabilities.java"));

        assertTrue(source.contains("event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.BIOGAS_POND_PORT.get(),"),
                "Biogas pond item input/output ports must expose an ItemHandler capability to item pipes");
    }
}

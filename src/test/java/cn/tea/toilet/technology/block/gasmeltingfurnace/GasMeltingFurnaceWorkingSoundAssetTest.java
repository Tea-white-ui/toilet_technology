package cn.tea.toilet.technology.block.gasmeltingfurnace;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GasMeltingFurnaceWorkingSoundAssetTest {
    private static final Path WORKING_SOUND = Path.of(
            "src/main/resources/assets/toilet_technology/sounds/gas_melting_furnace_working.ogg");

    @Test
    void providesEnoughContinuousAudioToAvoidAFrequentLoopBoundary() throws IOException {
        assertTrue(Files.size(WORKING_SOUND) >= 1_000_000,
                "The working loop must contain at least about one minute of crossfaded audio.");
    }
}

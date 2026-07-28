package cn.tea.toilet.technology.block.gasmeltingfurnace;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GasMeltingFurnaceWorkingSoundTest {
    @Test
    void playsTheWorkingSoundPeriodicallyWhileProcessing() throws IOException {
        String source = Files.readString(Path.of(
                "src/main/java/cn/tea/toilet/technology/block/gasmeltingfurnace/GasMeltingFurnaceBlockEntity.java"));

        assertTrue(source.contains("ModSounds.GAS_MELTING_FURNACE_WORKING.get()"));
        assertTrue(source.contains("SoundSource.BLOCKS"));
        assertTrue(source.contains("WORKING_SOUND_INTERVAL_TICKS"));
    }
}

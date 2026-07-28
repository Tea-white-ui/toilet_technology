package cn.tea.toilet.technology.block.gasmeltingfurnace;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GasMeltingFurnaceWorkingSoundTest {
    @Test
    void loopsAtReducedVolumeWhileTheFurnaceBlockIsLit() throws IOException {
        String blockEntity = Files.readString(Path.of(
                "src/main/java/cn/tea/toilet/technology/block/gasmeltingfurnace/GasMeltingFurnaceBlockEntity.java"));
        String clientSound = Files.readString(Path.of(
                "src/main/java/cn/tea/toilet/technology/client/GasMeltingFurnaceWorkingSound.java"));
        String soundTicker = Files.readString(Path.of(
                "src/main/java/cn/tea/toilet/technology/client/GasMeltingFurnaceSoundTicker.java"));

        assertFalse(blockEntity.contains("level.playSound("));
        assertTrue(clientSound.contains("looping = true"));
        assertTrue(clientSound.contains("volume = 0.125F"));
        assertTrue(clientSound.contains("attenuation = Attenuation.LINEAR"));
        assertTrue(clientSound.contains("GasMeltingFurnaceBlock.LIT"));
        assertTrue(clientSound.contains("stop();"));
        assertTrue(soundTicker.contains("queueTickingSound"));
        String soundDefinitions = Files.readString(Path.of(
                "src/main/java/cn/tea/toilet/technology/datagen/ModSoundProvider.java"));
        assertTrue(soundDefinitions.contains(
                "sound(\"toilet_technology:gas_melting_furnace_working\").preload().attenuationDistance(18)"));
    }
}

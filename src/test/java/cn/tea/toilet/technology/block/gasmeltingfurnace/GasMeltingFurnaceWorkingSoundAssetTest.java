package cn.tea.toilet.technology.block.gasmeltingfurnace;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GasMeltingFurnaceWorkingSoundAssetTest {
    private static final Path WORKING_SOUND = Path.of(
            "src/main/resources/assets/toilet_technology/sounds/gas_melting_furnace_working.ogg");

    @Test
    void usesMonoAudioSoOpenALCanApplyDistanceAttenuation() throws Exception {
        byte[] ogg = Files.readAllBytes(WORKING_SOUND);
        int segmentCount = Byte.toUnsignedInt(ogg[26]);
        int firstPacketOffset = 27 + segmentCount;

        assertEquals(1, Byte.toUnsignedInt(ogg[firstPacketOffset + 11]),
                "OpenAL only applies positional attenuation to mono sound files; stereo sounds play at full volume.");
    }
}

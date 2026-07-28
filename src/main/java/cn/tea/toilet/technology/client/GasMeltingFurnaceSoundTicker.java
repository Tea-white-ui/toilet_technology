package cn.tea.toilet.technology.client;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.block.gasmeltingfurnace.GasMeltingFurnaceBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** Starts one client-only looping sound per nearby lit gas melting furnace. */
public final class GasMeltingFurnaceSoundTicker {
    private static final int SOUND_SCAN_RADIUS_CHUNKS = 12;
    private static final int SOUND_SCAN_INTERVAL_TICKS = 10;
    private static final Map<BlockPos, GasMeltingFurnaceWorkingSound> ACTIVE_SOUNDS = new HashMap<>();

    private GasMeltingFurnaceSoundTicker() {
    }

    public static void tick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null || minecraft.player == null || minecraft.isPaused()) return;

        if (level.getGameTime() % SOUND_SCAN_INTERVAL_TICKS == 0) {
            int centerChunkX = minecraft.player.chunkPosition().x;
            int centerChunkZ = minecraft.player.chunkPosition().z;
            for (int chunkX = centerChunkX - SOUND_SCAN_RADIUS_CHUNKS; chunkX <= centerChunkX + SOUND_SCAN_RADIUS_CHUNKS; chunkX++) {
                for (int chunkZ = centerChunkZ - SOUND_SCAN_RADIUS_CHUNKS; chunkZ <= centerChunkZ + SOUND_SCAN_RADIUS_CHUNKS; chunkZ++) {
                    LevelChunk chunk = level.getChunkSource().getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
                    if (chunk == null) continue;
                    for (BlockPos pos : chunk.getBlockEntities().keySet()) {
                        if (level.getBlockState(pos).is(ModBlocks.GAS_MELTING_FURNACE.get())
                                && level.getBlockState(pos).getValue(GasMeltingFurnaceBlock.LIT)
                                && !ACTIVE_SOUNDS.containsKey(pos)) {
                            GasMeltingFurnaceWorkingSound sound = new GasMeltingFurnaceWorkingSound(level, pos);
                            ACTIVE_SOUNDS.put(pos.immutable(), sound);
                            minecraft.getSoundManager().queueTickingSound(sound);
                        }
                    }
                }
            }
        }
        Iterator<GasMeltingFurnaceWorkingSound> sounds = ACTIVE_SOUNDS.values().iterator();
        while (sounds.hasNext()) {
            if (sounds.next().isStopped()) {
                sounds.remove();
            }
        }
    }
}

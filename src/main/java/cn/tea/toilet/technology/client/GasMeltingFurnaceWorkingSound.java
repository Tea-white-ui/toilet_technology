package cn.tea.toilet.technology.client;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.block.gasmeltingfurnace.GasMeltingFurnaceBlock;
import cn.tea.toilet.technology.sound.ModSounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

/** Client-side loop which ends as soon as its furnace is no longer working. */
public final class GasMeltingFurnaceWorkingSound extends AbstractTickableSoundInstance {
    private final ClientLevel level;
    private final BlockPos pos;

    public GasMeltingFurnaceWorkingSound(ClientLevel level, BlockPos pos) {
        super(ModSounds.GAS_MELTING_FURNACE_WORKING.get(), SoundSource.BLOCKS, RandomSource.create());
        this.level = level;
        this.pos = pos.immutable();
        x = pos.getX() + 0.5D;
        y = pos.getY() + 0.5D;
        z = pos.getZ() + 0.5D;
        volume = 0.125F;
        pitch = 1.0F;
        looping = true;
        attenuation = Attenuation.LINEAR;
    }

    @Override
    public void tick() {
        if (!level.getBlockState(pos).is(ModBlocks.GAS_MELTING_FURNACE.get())
                || !level.getBlockState(pos).getValue(GasMeltingFurnaceBlock.LIT)) {
            stop();
        }
    }
}

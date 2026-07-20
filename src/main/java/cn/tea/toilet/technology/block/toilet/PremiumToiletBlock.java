package cn.tea.toilet.technology.block.toilet;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public  class PremiumToiletBlock extends ToiletBlock {

    public static final MapCodec<PremiumToiletBlock> CODEC = simpleCodec(PremiumToiletBlock::new);

    public PremiumToiletBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PremiumToiletBlockEntity(pos, state);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

}

package cn.tea.toilet.technology.block.toilet;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SquatToiletBlock extends ToiletBlock {
    public static final MapCodec<SquatToiletBlock> CODEC = simpleCodec(SquatToiletBlock::new);

    public SquatToiletBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
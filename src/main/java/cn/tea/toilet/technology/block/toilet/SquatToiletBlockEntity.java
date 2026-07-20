package cn.tea.toilet.technology.block.toilet;

import cn.tea.toilet.technology.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SquatToiletBlockEntity extends BlockEntity {

    public SquatToiletBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TOILET.get(), pos, blockState);
    }
}
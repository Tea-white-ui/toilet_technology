package cn.tea.toilet.technology.block.absorptiontower;

import cn.tea.toilet.technology.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class AbsorptionTowerBodyBlockEntity extends BlockEntity {
    public AbsorptionTowerBodyBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ABSORPTION_TOWER_BODY.get(), pos, state);
    }

    @Nullable
    private AbsorptionTowerBottomBlockEntity controller() {
        if (level == null) return null;
        BlockEntity entity = level.getBlockEntity(worldPosition.below(getBottomDistance()));
        return entity instanceof AbsorptionTowerBottomBlockEntity bottom ? bottom : null;
    }

    private int getBottomDistance() {
        for (int distance = 1; distance < AbsorptionTowerStructure.HEIGHT; distance++) {
            if (level != null && level.getBlockState(worldPosition.below(distance))
                    .is(cn.tea.toilet.technology.block.ModBlocks.ABSORPTION_TOWER_BOTTOM.get())) {
                return distance;
            }
        }
        return 1;
    }

    @Nullable
    public IFluidHandler getFluidHandler(@Nullable Direction side) {
        AbsorptionTowerBottomBlockEntity bottom = controller();
        return bottom == null ? null : bottom.getFluidOutputHandler(side);
    }

    @Nullable
    public cn.tea.toilet.technology.gas.IGasHandler getGasHandler(@Nullable Direction side) {
        AbsorptionTowerBottomBlockEntity bottom = controller();
        return bottom == null || getBottomDistance() != AbsorptionTowerStructure.HEIGHT - 1
                ? null : bottom.getGasOutputHandler(side);
    }
}

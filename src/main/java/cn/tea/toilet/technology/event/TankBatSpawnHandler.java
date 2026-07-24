package cn.tea.toilet.technology.event;

import cn.tea.toilet.technology.block.biogaspond.BiogasPondControllerBlockEntity;
import cn.tea.toilet.technology.block.biogaspond.BiogasPondPattern;
import cn.tea.toilet.technology.block.septictank.SepticTankControllerBlockEntity;
import cn.tea.toilet.technology.block.septictank.SepticTankPattern;
import cn.tea.toilet.technology.block.septictank.SepticTankControllerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

/** Prevents vanilla bats from occupying the intentionally dark interiors of the two tanks. */
public final class TankBatSpawnHandler {
    @SubscribeEvent
    public void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Bat bat) || !isInsideTankInterior(event.getLevel(), bat.blockPosition())) {
            return;
        }
        event.setCanceled(true);
    }

    private static boolean isInsideTankInterior(Level level, BlockPos entityPos) {
        for (BlockPos candidate : BlockPos.betweenClosed(
                entityPos.offset(-4, -5, -4), entityPos.offset(4, 1, 4))) {
            if (level.getBlockEntity(candidate) instanceof BiogasPondControllerBlockEntity) {
                if (contains(BiogasPondPattern.layout().air(), candidate, entityPos)) return true;
            } else if (level.getBlockEntity(candidate) instanceof SepticTankControllerBlockEntity septicTank) {
                SepticTankPattern.Facing facing = switch (septicTank.getBlockState()
                        .getValue(SepticTankControllerBlock.FACING)) {
                    case SOUTH -> SepticTankPattern.Facing.SOUTH;
                    case WEST -> SepticTankPattern.Facing.WEST;
                    case EAST -> SepticTankPattern.Facing.EAST;
                    default -> SepticTankPattern.Facing.NORTH;
                };
                if (contains(SepticTankPattern.layout(facing).air(), candidate, entityPos)) return true;
            }
        }
        return false;
    }

    private static boolean contains(java.util.Set<? extends Object> cells, BlockPos controllerPos, BlockPos entityPos) {
        return cells.stream().anyMatch(cell -> {
            if (cell instanceof BiogasPondPattern.Cell c) {
                return controllerPos.offset(c.x(), c.y(), c.z()).equals(entityPos);
            }
            if (cell instanceof SepticTankPattern.Cell c) {
                return controllerPos.offset(c.x(), c.y(), c.z()).equals(entityPos);
            }
            return false;
        });
    }
}

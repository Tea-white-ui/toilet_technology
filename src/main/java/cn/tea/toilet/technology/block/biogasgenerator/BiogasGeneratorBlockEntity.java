package cn.tea.toilet.technology.block.biogasgenerator;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;

public class BiogasGeneratorBlockEntity extends BlockEntity {
    private static final String ENERGY_TAG = "Energy";

    public final GeneratorEnergyStorage energyStorage = new GeneratorEnergyStorage();

    public class GeneratorEnergyStorage extends EnergyStorage {
        private GeneratorEnergyStorage() {
            super(BiogasGeneratorOperation.ENERGY_CAPACITY,
                    BiogasGeneratorOperation.ENERGY_CAPACITY, 0);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = super.receiveEnergy(maxReceive, simulate);
            if (received > 0 && !simulate) setChanged();
            return received;
        }

        private void consumePerTick() {
            energy -= BiogasGeneratorOperation.ENERGY_PER_TICK;
        }

        private void restoreEnergy(int storedEnergy) {
            energy = storedEnergy;
        }
    }

    public BiogasGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BIOGAS_GENERATOR.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BiogasGeneratorBlockEntity entity) {
        if (!state.hasProperty(BiogasGeneratorBlock.FACING)) return;
        BlockPos backPos = pos.relative(state.getValue(BiogasGeneratorBlock.FACING).getOpposite());
        boolean backTouchesWall = level.getBlockState(backPos).is(ModBlocks.SEPTIC_TANK_WALL.get());
        if (BiogasGeneratorOperation.canRun(backTouchesWall, entity.energyStorage.getEnergyStored())) {
            entity.energyStorage.consumePerTick();
            entity.setChanged();
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt(ENERGY_TAG, energyStorage.getEnergyStored());
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        int stored = Math.clamp(tag.getInt(ENERGY_TAG), 0, energyStorage.getMaxEnergyStored());
        energyStorage.restoreEnergy(stored);
    }
}
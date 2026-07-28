package cn.tea.toilet.technology.block.gasgenerators;

import cn.tea.toilet.technology.api.gas.BasicGasTank;
import cn.tea.toilet.technology.api.gas.GasAction;
import cn.tea.toilet.technology.api.gas.GasStack;
import cn.tea.toilet.technology.api.gas.IGasHandler;
import cn.tea.toilet.technology.block.automation.EnergyTransfer;
import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.gas.GasRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Burns biogas or methane to produce FE. Every face accepts gas and exports energy. */
public final class GasGeneratorsBlockEntity extends BlockEntity {
    private static final String ENERGY_TAG = "Energy";
    private static final String GAS_TANK_TAG = "GasTank";

    private final BasicGasTank gasTank = new BasicGasTank(GasGeneratorOperation.GAS_CAPACITY,
            stack -> stack.is(GasRegistry.BIOGAS) || stack.is(GasRegistry.METHANE), this::setChanged);
    private final GeneratorEnergyStorage energyStorage = new GeneratorEnergyStorage();
    private final IGasHandler gasHandler = new GeneratorGasHandler();

    public GasGeneratorsBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GAS_GENERATORS.get(), pos, state);
    }

    public @Nullable IGasHandler getGasHandler(@Nullable Direction side) {
        return gasHandler;
    }

    public @Nullable EnergyStorage getEnergyStorage(@Nullable Direction side) {
        return energyStorage;
    }

    public GasStack getGasStack() {
        return gasTank.getStack();
    }

    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    public boolean isGenerating() {
        GasStack stack = gasTank.getStack();
        GasGeneratorOperation.Fuel fuel = stack.isEmpty() ? null : GasGeneratorOperation.forGasId(stack.getGas().id().toString());
        return GasGeneratorOperation.canGenerate(fuel, stack.getAmount(), energyStorage.getEnergyStored());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GasGeneratorsBlockEntity entity) {
        if (level.isClientSide()) return;
        GasStack stack = entity.gasTank.getStack();
        GasGeneratorOperation.Fuel fuel = stack.isEmpty() ? null : GasGeneratorOperation.forGasId(stack.getGas().id().toString());
        if (GasGeneratorOperation.canGenerate(fuel, stack.getAmount(), entity.energyStorage.getEnergyStored())) {
            entity.gasTank.extract(1, GasAction.EXECUTE);
            entity.energyStorage.generate(fuel.energyPerTick());
            entity.setChanged();
        }
        entity.pushEnergy(level, pos);
    }

    private void pushEnergy(Level level, BlockPos pos) {
        for (Direction side : Direction.values()) {
            IEnergyStorage destination = level.getCapability(Capabilities.EnergyStorage.BLOCK,
                    pos.relative(side), side.getOpposite());
            if (destination != null) {
                EnergyTransfer.transfer(energyStorage, destination, GasGeneratorOperation.ENERGY_CAPACITY);
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(GAS_TANK_TAG, gasTank.serializeNBT());
        tag.putInt(ENERGY_TAG, energyStorage.getEnergyStored());
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(GAS_TANK_TAG)) gasTank.deserializeNBT(tag.getCompound(GAS_TANK_TAG));
        energyStorage.restoreEnergy(Math.clamp(tag.getInt(ENERGY_TAG), 0, GasGeneratorOperation.ENERGY_CAPACITY));
    }

    private final class GeneratorGasHandler implements IGasHandler {
        @Override public int getGasTanks() { return 1; }
        @Override public GasStack getGasInTank(int tank) { return tank == 0 ? gasTank.getStack() : GasStack.EMPTY; }
        @Override public void setGasInTank(int tank, GasStack stack) { if (tank == 0) gasTank.setStack(stack); }
        @Override public long getGasTankCapacity(int tank) { return tank == 0 ? GasGeneratorOperation.GAS_CAPACITY : 0; }
        @Override public boolean isValid(int tank, GasStack stack) { return tank == 0 && gasTank.isValid(stack); }
        @Override public GasStack insertGas(int tank, GasStack stack, GasAction action) {
            return tank == 0 ? gasTank.insert(stack, action) : stack;
        }
        @Override public GasStack extractGas(int tank, long amount, GasAction action) { return GasStack.EMPTY; }
    }

    private final class GeneratorEnergyStorage extends EnergyStorage {
        private GeneratorEnergyStorage() {
            super(GasGeneratorOperation.ENERGY_CAPACITY, 0, GasGeneratorOperation.ENERGY_CAPACITY);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int extracted = super.extractEnergy(maxExtract, simulate);
            if (extracted > 0 && !simulate) setChanged();
            return extracted;
        }

        private void generate(int amount) {
            energy = Math.min(energy + amount, getMaxEnergyStored());
        }

        private void restoreEnergy(int amount) {
            energy = amount;
        }
    }
}

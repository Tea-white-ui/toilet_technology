package cn.tea.toilet.technology.block.sewagepurifier;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.compat.PoopSkyCompat;
import cn.tea.toilet.technology.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SewagePurifierBlockEntity extends BlockEntity {
    private static final String INPUT_TANK_TAG = "InputTank";
    private static final String OUTPUT_TANK_TAG = "OutputTank";
    private static final String ENERGY_TAG = "Energy";

    public final FluidTank inputTank = new FluidTank(SewagePurifierOperation.TANK_CAPACITY,
            SewagePurifierBlockEntity::isFecesLiquid) {
        @Override protected void onContentsChanged() { setChanged(); }
    };
    public final FluidTank outputTank = new FluidTank(SewagePurifierOperation.TANK_CAPACITY,
            SewagePurifierBlockEntity::isWater) {
        @Override protected void onContentsChanged() { setChanged(); }
    };
    public final PurifierEnergyStorage energyStorage = new PurifierEnergyStorage();
    private final IFluidHandler inputFluidHandler = new InputFluidHandler();
    private final IFluidHandler outputFluidHandler = new OutputFluidHandler();

    public SewagePurifierBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SEWAGE_PURIFIER.get(), pos, state);
    }

    public @Nullable IFluidHandler getFluidHandler(@Nullable Direction side) {
        if (side == null || !getBlockState().hasProperty(SewagePurifierBlock.FACING)) return null;
        Direction facing = getBlockState().getValue(SewagePurifierBlock.FACING);
        if (side == facing) return outputFluidHandler;
        return side == facing.getOpposite() ? inputFluidHandler : null;
    }

    public @Nullable EnergyStorage getEnergyStorage(@Nullable Direction side) {
        if (side == null || !getBlockState().hasProperty(SewagePurifierBlock.FACING)) return null;
        Direction facing = getBlockState().getValue(SewagePurifierBlock.FACING);
        return side == facing.getClockWise() || side == facing.getCounterClockWise() ? energyStorage : null;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SewagePurifierBlockEntity entity) {
        if (level.isClientSide()) return;
        FluidStack input = entity.inputTank.getFluid();
        int outputSpace = SewagePurifierOperation.TANK_CAPACITY - entity.outputTank.getFluidAmount();
        FecesOutput output = entity.findFecesOutput(level, pos);
        if (!isFecesLiquid(input) || !SewagePurifierOperation.canProcess(input.getAmount(),
                entity.energyStorage.getEnergyStored(), outputSpace, output.canAccept())) return;

        entity.inputTank.drain(SewagePurifierOperation.BATCH_FLUID, IFluidHandler.FluidAction.EXECUTE);
        entity.outputTank.fill(new FluidStack(Fluids.WATER, SewagePurifierOperation.BATCH_FLUID), IFluidHandler.FluidAction.EXECUTE);
        entity.energyStorage.consumeBatch();
        output.execute(level, pos);
        entity.setChanged();
    }

    private FecesOutput findFecesOutput(Level level, BlockPos pos) {
        BlockPos below = pos.below();
        if (level.getBlockState(below).isAir()) return FecesOutput.drop();
        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, below, Direction.UP);
        if (handler == null) return FecesOutput.blocked();
        ItemStack remaining = ModItems.FECES.get().getDefaultInstance().copyWithCount(SewagePurifierOperation.FECES_PER_BATCH);
        for (int slot = 0; slot < handler.getSlots() && !remaining.isEmpty(); slot++) {
            remaining = handler.insertItem(slot, remaining, true);
        }
        return remaining.isEmpty() ? FecesOutput.container(handler) : FecesOutput.blocked();
    }

    private static boolean isFecesLiquid(FluidStack stack) {
        return PoopSkyCompat.isFecesLiquid(stack);
    }

    private static boolean isWater(FluidStack stack) {
        return stack.is(Fluids.WATER) || stack.is(Fluids.FLOWING_WATER);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(INPUT_TANK_TAG, inputTank.writeToNBT(registries, new CompoundTag()));
        tag.put(OUTPUT_TANK_TAG, outputTank.writeToNBT(registries, new CompoundTag()));
        tag.putInt(ENERGY_TAG, energyStorage.getEnergyStored());
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(INPUT_TANK_TAG)) inputTank.readFromNBT(registries, tag.getCompound(INPUT_TANK_TAG));
        if (tag.contains(OUTPUT_TANK_TAG)) outputTank.readFromNBT(registries, tag.getCompound(OUTPUT_TANK_TAG));
        energyStorage.restoreEnergy(Math.clamp(tag.getInt(ENERGY_TAG), 0, energyStorage.getMaxEnergyStored()));
    }

    public class PurifierEnergyStorage extends EnergyStorage {
        private PurifierEnergyStorage() {
            super(SewagePurifierOperation.ENERGY_CAPACITY, SewagePurifierOperation.ENERGY_CAPACITY, 0);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = super.receiveEnergy(maxReceive, simulate);
            if (received > 0 && !simulate) setChanged();
            return received;
        }

        private void consumeBatch() {
            energy = SewagePurifierOperation.energyAfterProcess(energy);
        }

        private void restoreEnergy(int storedEnergy) {
            energy = storedEnergy;
        }
    }

    private class InputFluidHandler implements IFluidHandler {
        @Override public int getTanks() { return 1; }
        @Override public @NotNull FluidStack getFluidInTank(int tank) { return tank == 0 ? inputTank.getFluid() : FluidStack.EMPTY; }
        @Override public int getTankCapacity(int tank) { return tank == 0 ? inputTank.getCapacity() : 0; }
        @Override public boolean isFluidValid(int tank, @NotNull FluidStack stack) { return tank == 0 && isFecesLiquid(stack); }
        @Override public int fill(FluidStack stack, FluidAction action) { return inputTank.fill(stack, action); }
        @Override public @NotNull FluidStack drain(FluidStack stack, FluidAction action) { return FluidStack.EMPTY; }
        @Override public @NotNull FluidStack drain(int amount, FluidAction action) { return FluidStack.EMPTY; }
    }

    private class OutputFluidHandler implements IFluidHandler {
        @Override public int getTanks() { return 1; }
        @Override public @NotNull FluidStack getFluidInTank(int tank) { return tank == 0 ? outputTank.getFluid() : FluidStack.EMPTY; }
        @Override public int getTankCapacity(int tank) { return tank == 0 ? outputTank.getCapacity() : 0; }
        @Override public boolean isFluidValid(int tank, @NotNull FluidStack stack) { return false; }
        @Override public int fill(FluidStack stack, FluidAction action) { return 0; }
        @Override public @NotNull FluidStack drain(FluidStack stack, FluidAction action) { return outputTank.drain(stack, action); }
        @Override public @NotNull FluidStack drain(int amount, FluidAction action) { return outputTank.drain(amount, action); }
    }

    private record FecesOutput(@Nullable IItemHandler container, boolean canAccept) {
        private static FecesOutput drop() { return new FecesOutput(null, true); }
        private static FecesOutput container(IItemHandler handler) { return new FecesOutput(handler, true); }
        private static FecesOutput blocked() { return new FecesOutput(null, false); }

        private void execute(Level level, BlockPos pos) {
            ItemStack feces = ModItems.FECES.get().getDefaultInstance().copyWithCount(SewagePurifierOperation.FECES_PER_BATCH);
            if (container != null) {
                for (int slot = 0; slot < container.getSlots() && !feces.isEmpty(); slot++) {
                    feces = container.insertItem(slot, feces, false);
                }
                return;
            }
            for (int count = 0; count < SewagePurifierOperation.FECES_PER_BATCH; count++) {
                level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() - 0.2, pos.getZ() + 0.5,
                        ModItems.FECES.get().getDefaultInstance()));
            }
        }
    }
}

package cn.tea.toilet.technology.block.absorptiontower;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.gas.BasicGasTank;
import cn.tea.toilet.technology.gas.GasAction;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.gas.IGasHandler;
import cn.tea.toilet.technology.recipe.AbsorptionTowerRecipe;
import cn.tea.toilet.technology.recipe.AbsorptionTowerRecipeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AbsorptionTowerBottomBlockEntity extends BlockEntity {
    public static final int TANK_CAPACITY = 8_000;

    private boolean structureValid;
    private int validationCooldown = 1;
    private final FluidTank waterInputTank = createWaterTank();
    private final FluidTank liquidOutputTank = createOutputTank();
    private final BasicGasTank gasInputTank = new BasicGasTank(TANK_CAPACITY, stack -> true, this::setChanged);
    private final BasicGasTank gasOutputTank = new BasicGasTank(TANK_CAPACITY, stack -> true, this::setChanged);
    private final IFluidHandler waterInputHandler = new InputFluidHandler(waterInputTank);
    private final IFluidHandler liquidOutputHandler = new OutputFluidHandler(liquidOutputTank);
    private final IGasHandler gasInputHandler = new InputGasHandler(gasInputTank);
    private final IGasHandler gasOutputHandler = new OutputGasHandler(gasOutputTank);

    public AbsorptionTowerBottomBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ABSORPTION_TOWER_BOTTOM.get(), pos, state);
    }

    private FluidTank createWaterTank() {
        return new FluidTank(TANK_CAPACITY, stack -> stack.getFluid().isSame(Fluids.WATER)) {
            @Override protected void onContentsChanged() { setChanged(); }
        };
    }

    private FluidTank createOutputTank() {
        return new FluidTank(TANK_CAPACITY) {
            @Override protected void onContentsChanged() { setChanged(); }
        };
    }

    public boolean revalidateStructure() {
        if (level == null) return false;
        boolean valid = AbsorptionTowerStructure.validate(level, worldPosition);
        if (structureValid != valid) {
            structureValid = valid;
            setChanged();
            invalidateTowerCapabilities();
        }
        validationCooldown = 20;
        return valid;
    }

    public boolean isStructureValid() {
        return structureValid;
    }

    public FluidStack getWaterInput() {
        return waterInputTank.getFluid();
    }

    public FluidStack getLiquidOutput() {
        return liquidOutputTank.getFluid();
    }

    public GasStack getGasInput() {
        return gasInputTank.getStack();
    }

    public GasStack getGasOutput() {
        return gasOutputTank.getStack();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AbsorptionTowerBottomBlockEntity entity) {
        if (level.isClientSide()) return;
        if (--entity.validationCooldown <= 0) entity.revalidateStructure();
        if (entity.structureValid) entity.processOneRecipe();
    }

    private void processOneRecipe() {
        GasStack inputGas = gasInputTank.getStack();
        FluidStack inputWater = waterInputTank.getFluid();
        if (inputGas.isEmpty() || inputWater.isEmpty()) return;

        AbsorptionTowerRecipe recipe = AbsorptionTowerRecipeRegistry.find(inputGas.gas(), inputWater.getFluid());
        if (recipe == null) return;

        long gasOutputSpace = gasOutputTank.getCapacity() - gasOutputTank.getStored();
        int liquidOutputSpace = liquidOutputTank.getCapacity() - liquidOutputTank.getFluidAmount();
        if (!AbsorptionTowerOperation.canProcess(inputGas.amount(), inputWater.getAmount(), gasOutputSpace, liquidOutputSpace)) return;

        GasStack gasRemainder = gasOutputTank.insert(new GasStack(recipe.outputGas(), recipe.outputGasAmount()), GasAction.SIMULATE);
        int acceptedLiquid = liquidOutputTank.fill(new FluidStack(recipe.outputFluid(), recipe.outputFluidAmount()), IFluidHandler.FluidAction.SIMULATE);
        if (!gasRemainder.isEmpty() || acceptedLiquid != recipe.outputFluidAmount()) return;

        gasInputTank.extract(recipe.inputGasAmount(), GasAction.EXECUTE);
        waterInputTank.drain(recipe.inputFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
        gasOutputTank.insert(new GasStack(recipe.outputGas(), recipe.outputGasAmount()), GasAction.EXECUTE);
        liquidOutputTank.fill(new FluidStack(recipe.outputFluid(), recipe.outputFluidAmount()), IFluidHandler.FluidAction.EXECUTE);
        setChanged();
    }

    @Nullable
    public IFluidHandler getFluidHandler(@Nullable Direction side) {
        if (!structureValid || side == null) return null;
        Direction front = getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        if (side == front || side.getAxis().isHorizontal()) return waterInputHandler;
        return null;
    }

    @Nullable
    public IGasHandler getGasHandler(@Nullable Direction side) {
        if (!structureValid || side == null) return null;
        return side == Direction.DOWN ? gasInputHandler : null;
    }

    @Nullable
    public IFluidHandler getFluidOutputHandler(@Nullable Direction side) {
        if (!structureValid || side == null || side == Direction.UP || side == Direction.DOWN) return null;
        return liquidOutputHandler;
    }

    @Nullable
    public IGasHandler getGasOutputHandler(@Nullable Direction side) {
        return structureValid && side == Direction.UP ? gasOutputHandler : null;
    }

    private void invalidateTowerCapabilities() {
        if (level == null) return;
        for (int y = 0; y < AbsorptionTowerStructure.HEIGHT; y++) {
            level.invalidateCapabilities(worldPosition.above(y));
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("WaterInputTank", waterInputTank.writeToNBT(registries, new CompoundTag()));
        tag.put("LiquidOutputTank", liquidOutputTank.writeToNBT(registries, new CompoundTag()));
        tag.put("GasInputTank", gasInputTank.serializeNBT());
        tag.put("GasOutputTank", gasOutputTank.serializeNBT());
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("WaterInputTank")) waterInputTank.readFromNBT(registries, tag.getCompound("WaterInputTank"));
        if (tag.contains("LiquidOutputTank")) liquidOutputTank.readFromNBT(registries, tag.getCompound("LiquidOutputTank"));
        if (tag.contains("GasInputTank")) gasInputTank.deserializeNBT(tag.getCompound("GasInputTank"));
        if (tag.contains("GasOutputTank")) gasOutputTank.deserializeNBT(tag.getCompound("GasOutputTank"));
        structureValid = false;
        validationCooldown = 1;
    }

    private final class InputFluidHandler implements IFluidHandler {
        private final FluidTank tank;
        private InputFluidHandler(FluidTank tank) { this.tank = tank; }
        @Override public int getTanks() { return 1; }
        @Override public @NotNull FluidStack getFluidInTank(int tank) { return tank == 0 ? this.tank.getFluid() : FluidStack.EMPTY; }
        @Override public int getTankCapacity(int tank) { return tank == 0 ? TANK_CAPACITY : 0; }
        @Override public boolean isFluidValid(int tank, @NotNull FluidStack stack) { return tank == 0 && stack.getFluid().isSame(Fluids.WATER); }
        @Override public int fill(FluidStack stack, FluidAction action) { return stack.getFluid().isSame(Fluids.WATER) ? tank.fill(stack, action) : 0; }
        @Override public @NotNull FluidStack drain(FluidStack stack, FluidAction action) { return FluidStack.EMPTY; }
        @Override public @NotNull FluidStack drain(int amount, FluidAction action) { return FluidStack.EMPTY; }
    }

    private final class OutputFluidHandler implements IFluidHandler {
        private final FluidTank tank;
        private OutputFluidHandler(FluidTank tank) { this.tank = tank; }
        @Override public int getTanks() { return 1; }
        @Override public @NotNull FluidStack getFluidInTank(int tank) { return tank == 0 ? this.tank.getFluid() : FluidStack.EMPTY; }
        @Override public int getTankCapacity(int tank) { return tank == 0 ? TANK_CAPACITY : 0; }
        @Override public boolean isFluidValid(int tank, @NotNull FluidStack stack) { return false; }
        @Override public int fill(FluidStack stack, FluidAction action) { return 0; }
        @Override public @NotNull FluidStack drain(FluidStack stack, FluidAction action) { return this.tank.drain(stack, action); }
        @Override public @NotNull FluidStack drain(int amount, FluidAction action) { return tank.drain(amount, action); }
    }

    private final class InputGasHandler implements IGasHandler {
        private final BasicGasTank tank;
        private InputGasHandler(BasicGasTank tank) { this.tank = tank; }
        @Override public int getGasTanks() { return 1; }
        @Override public GasStack getGasInTank(int tank) { return tank == 0 ? this.tank.getStack() : GasStack.EMPTY; }
        @Override public void setGasInTank(int tank, GasStack stack) { }
        @Override public long getGasTankCapacity(int tank) { return tank == 0 ? TANK_CAPACITY : 0; }
        @Override public boolean isValid(int tank, GasStack stack) { return tank == 0 && this.tank.isValid(stack); }
        @Override public GasStack insertGas(int tank, GasStack stack, GasAction action) { return tank == 0 ? this.tank.insert(stack, action) : stack; }
        @Override public GasStack extractGas(int tank, long amount, GasAction action) { return GasStack.EMPTY; }
    }

    private final class OutputGasHandler implements IGasHandler {
        private final BasicGasTank tank;
        private OutputGasHandler(BasicGasTank tank) { this.tank = tank; }
        @Override public int getGasTanks() { return 1; }
        @Override public GasStack getGasInTank(int tank) { return tank == 0 ? this.tank.getStack() : GasStack.EMPTY; }
        @Override public void setGasInTank(int tank, GasStack stack) { }
        @Override public long getGasTankCapacity(int tank) { return tank == 0 ? TANK_CAPACITY : 0; }
        @Override public boolean isValid(int tank, GasStack stack) { return false; }
        @Override public GasStack insertGas(int tank, GasStack stack, GasAction action) { return stack; }
        @Override public GasStack extractGas(int tank, long amount, GasAction action) { return tank == 0 ? this.tank.extract(amount, action) : GasStack.EMPTY; }
    }
}

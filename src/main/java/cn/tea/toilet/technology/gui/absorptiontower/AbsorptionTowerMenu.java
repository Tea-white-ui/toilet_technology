package cn.tea.toilet.technology.gui.absorptiontower;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.block.absorptiontower.AbsorptionTowerBottomBlockEntity;
import cn.tea.toilet.technology.gas.Gas;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.gui.ModMenuTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class AbsorptionTowerMenu extends AbstractContainerMenu {
    private static final int DATA_WATER_INPUT_AMOUNT = 0;
    private static final int DATA_LIQUID_OUTPUT_AMOUNT = 1;
    private static final int DATA_GAS_INPUT_AMOUNT = 2;
    private static final int DATA_GAS_OUTPUT_AMOUNT = 3;
    private static final int DATA_WATER_INPUT_TYPE = 4;
    private static final int DATA_LIQUID_OUTPUT_TYPE = 5;
    private static final int DATA_GAS_INPUT_TYPE = 6;
    private static final int DATA_GAS_OUTPUT_TYPE = 7;
    private static final int DATA_STRUCTURE_VALID = 8;
    private static final int DATA_COUNT = 9;
    private static final int PLAYER_INVENTORY_START = 0;
    private final ContainerLevelAccess access;
    private final ContainerData data;

    public AbsorptionTowerMenu(int id, Inventory inventory) {
        this(id, inventory, null, ContainerLevelAccess.NULL);
    }

    public AbsorptionTowerMenu(int id, Inventory inventory, AbsorptionTowerBottomBlockEntity controller,
                               ContainerLevelAccess access) {
        super(ModMenuTypes.ABSORPTION_TOWER_MENU.get(), id);
        this.access = access;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 150 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(inventory, column, 8 + column * 18, 208));
        }
        data = controller == null ? new SimpleContainerData(DATA_COUNT) : new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case DATA_WATER_INPUT_AMOUNT -> controller.getWaterInput().getAmount();
                    case DATA_LIQUID_OUTPUT_AMOUNT -> controller.getLiquidOutput().getAmount();
                    case DATA_GAS_INPUT_AMOUNT -> (int) controller.getGasInput().getAmount();
                    case DATA_GAS_OUTPUT_AMOUNT -> (int) controller.getGasOutput().getAmount();
                    case DATA_WATER_INPUT_TYPE -> fluidWireValue(controller.getWaterInput());
                    case DATA_LIQUID_OUTPUT_TYPE -> fluidWireValue(controller.getLiquidOutput());
                    case DATA_GAS_INPUT_TYPE -> gasWireValue(controller.getGasInput());
                    case DATA_GAS_OUTPUT_TYPE -> gasWireValue(controller.getGasOutput());
                    case DATA_STRUCTURE_VALID -> controller.isStructureValid() ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return DATA_COUNT;
            }
        };
        addDataSlots(data);
    }

    public FluidStack waterInput() {
        return displayFluid(DATA_WATER_INPUT_TYPE, data.get(DATA_WATER_INPUT_AMOUNT));
    }

    public FluidStack liquidOutput() {
        return displayFluid(DATA_LIQUID_OUTPUT_TYPE, data.get(DATA_LIQUID_OUTPUT_AMOUNT));
    }

    public GasStack gasInput() {
        return displayGas(DATA_GAS_INPUT_TYPE, data.get(DATA_GAS_INPUT_AMOUNT));
    }

    public GasStack gasOutput() {
        return displayGas(DATA_GAS_OUTPUT_TYPE, data.get(DATA_GAS_OUTPUT_AMOUNT));
    }

    public int tankCapacity() {
        return AbsorptionTowerBottomBlockEntity.TANK_CAPACITY;
    }

    private static int fluidWireValue(FluidStack stack) {
        return stack.isEmpty() ? 0 : BuiltInRegistries.FLUID.getId(stack.getFluid()) + 1;
    }

    private static int gasWireValue(GasStack stack) {
        return stack.isEmpty() ? 0 : GasRegistry.id(stack.getGas()) + 1;
    }

    private FluidStack displayFluid(int wireDataIndex, int amount) {
        int registryId = data.get(wireDataIndex) - 1;
        if (registryId < 0 || amount <= 0) return FluidStack.EMPTY;
        Fluid fluid = BuiltInRegistries.FLUID.byId(registryId);
        return new FluidStack(fluid, amount);
    }

    private GasStack displayGas(int wireDataIndex, int amount) {
        int gasId = data.get(wireDataIndex) - 1;
        if (gasId < 0 || amount <= 0) return GasStack.EMPTY;
        Gas gas = GasRegistry.byId(gasId);
        return gas == null ? GasStack.EMPTY : new GasStack(gas, amount);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return data.get(DATA_STRUCTURE_VALID) != 0 && stillValid(access, player, ModBlocks.ABSORPTION_TOWER_BOTTOM.get());
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        if (index < PLAYER_INVENTORY_START || index >= slots.size()) return ItemStack.EMPTY;
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack source = sourceSlot.getItem();
        ItemStack copy = source.copy();
        if (index < 27) {
            if (!moveItemStackTo(source, 27, 36, false)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(source, 0, 27, false)) {
            return ItemStack.EMPTY;
        }
        if (source.isEmpty()) sourceSlot.setByPlayer(ItemStack.EMPTY); else sourceSlot.setChanged();
        return copy;
    }
}
package cn.tea.toilet.technology.gui.biogaspond;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.block.biogaspond.BiogasPondControllerBlockEntity;
import cn.tea.toilet.technology.gui.ModMenuTypes;
import cn.tea.toilet.technology.gui.TankMenuData;
import cn.tea.toilet.technology.gui.septictank.OutputOnlySlot;
import cn.tea.toilet.technology.api.gas.Gas;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.api.gas.GasStack;
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
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class BiogasPondMenu extends AbstractContainerMenu {
    private static final int CUSTOM_SLOTS = 2;
    private static final int DATA_LIQUID_AMOUNT = 0;
    private static final int DATA_GAS_LOW = 1;
    private static final int DATA_GAS_HIGH = 2;
    private static final int DATA_STRUCTURE_VALID = 3;
    private static final int DATA_LIQUID_TYPE = 4;
    private static final int DATA_GAS_TYPE = 5;
    private static final int DATA_COUNT = 6;
    private final ContainerLevelAccess access;
    private final ContainerData data;

    public BiogasPondMenu(int id, Inventory inventory) { this(id, inventory, null, ContainerLevelAccess.NULL); }

    public BiogasPondMenu(int id, Inventory inventory, BiogasPondControllerBlockEntity controller, ContainerLevelAccess access) {
        super(ModMenuTypes.BIOGAS_POND_MENU.get(), id);
        this.access = access;
        IItemHandler handler = controller == null ? new ItemStackHandler(CUSTOM_SLOTS) : controller.getMenuItems();
        addSlot(new SlotItemHandler(handler, BiogasPondControllerBlockEntity.INPUT_SLOT, 152, 51));
        addSlot(new OutputOnlySlot(handler, BiogasPondControllerBlockEntity.OUTPUT_SLOT, 8, 86));
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++)
            addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 117 + row * 18));
        for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col, 8 + col * 18, 175));
        data = controller == null ? new SimpleContainerData(DATA_COUNT) : new ContainerData() {
            @Override public int get(int index) {
                return switch (index) {
                    case DATA_LIQUID_AMOUNT -> controller.liquidTank.getFluidAmount();
                    case DATA_GAS_LOW -> TankMenuData.lowWord(controller.gasTank.getStored());
                    case DATA_GAS_HIGH -> TankMenuData.highWord(controller.gasTank.getStored());
                    case DATA_STRUCTURE_VALID -> controller.isStructureValid() ? 1 : 0;
                    case DATA_LIQUID_TYPE -> fluidWireValue(controller.liquidTank.getFluid());
                    case DATA_GAS_TYPE -> chemicalWireValue(controller.gasTank.getStack());
                    default -> 0;
                };
            }
            @Override public void set(int index, int value) { }
            @Override public int getCount() { return DATA_COUNT; }
        };
        addDataSlots(data);
    }

    public int liquidAmount() { return data.get(DATA_LIQUID_AMOUNT); }
    public long gasAmount() { return TankMenuData.decodeLong(data.get(DATA_GAS_LOW), data.get(DATA_GAS_HIGH)); }
    public boolean structureValid() { return data.get(DATA_STRUCTURE_VALID) != 0; }
    public FluidStack liquidStack() { return displayFluidStack(DATA_LIQUID_TYPE, liquidAmount()); }
    public GasStack gasStack() { return displayGasStack(DATA_GAS_TYPE, gasAmount()); }

    private static int fluidWireValue(FluidStack stack) {
        return stack.isEmpty() ? 0 : TankMenuData.encodeRegistryId(BuiltInRegistries.FLUID.getId(stack.getFluid()));
    }

    private static int chemicalWireValue(GasStack stack) {
        return stack.isEmpty() ? 0 : TankMenuData.encodeRegistryId(
                GasRegistry.id(stack.gas()));
    }

    private FluidStack displayFluidStack(int dataIndex, int amount) {
        int registryId = TankMenuData.decodeRegistryId(data.get(dataIndex));
        if (registryId < 0 || amount <= 0) return FluidStack.EMPTY;
        Fluid fluid = BuiltInRegistries.FLUID.byId(registryId);
        return new FluidStack(fluid, amount);
    }

    private GasStack displayGasStack(int dataIndex, long amount) {
        int registryId = TankMenuData.decodeRegistryId(data.get(dataIndex));
        if (registryId < 0 || amount <= 0) return GasStack.EMPTY;
        Gas gas = GasRegistry.byId(registryId);
        return gas == null ? GasStack.EMPTY : new GasStack(gas, amount);
    }

    @Override public boolean stillValid(@NotNull Player player) {
        return structureValid() && stillValid(access, player, ModBlocks.BIOGAS_POND_CONTROLLER.get());
    }

    @Override public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        if (index < 0 || index >= slots.size() || !slots.get(index).hasItem()) return ItemStack.EMPTY;
        Slot slot = slots.get(index);
        ItemStack source = slot.getItem();
        ItemStack copy = source.copy();
        if (index < CUSTOM_SLOTS) {
            if (!moveItemStackTo(source, CUSTOM_SLOTS, slots.size(), true)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(source, BiogasPondControllerBlockEntity.INPUT_SLOT,
                BiogasPondControllerBlockEntity.INPUT_SLOT + 1, false)) {
            return ItemStack.EMPTY;
        }
        if (source.isEmpty()) slot.setByPlayer(ItemStack.EMPTY); else slot.setChanged();
        return copy;
    }
}

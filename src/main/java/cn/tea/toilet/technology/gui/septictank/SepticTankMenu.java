package cn.tea.toilet.technology.gui.septictank;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.block.septictank.SepticTankControllerBlockEntity;
import cn.tea.toilet.technology.gui.ModMenuTypes;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
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

public class SepticTankMenu extends AbstractContainerMenu {
    public static final int CUSTOM_SLOTS = SepticTankMenuLayout.CUSTOM_SLOT_COUNT;
    private static final int DATA_LIQUID_AMOUNT = 0;
    private static final int DATA_GAS_AMOUNT = 1;
    private static final int DATA_STRUCTURE_VALID = 2;
    private static final int DATA_LIQUID_TYPE = 3;
    private static final int DATA_GAS_TYPE = 4;
    private static final int DATA_COUNT = 5;
    private final ContainerLevelAccess access;
    private final ContainerData data;

    public SepticTankMenu(int id, Inventory inventory) { this(id, inventory, null, ContainerLevelAccess.NULL); }

    public SepticTankMenu(int id, Inventory inventory, SepticTankControllerBlockEntity controller, ContainerLevelAccess access) {
        super(ModMenuTypes.SEPTIC_TANK_MENU.get(), id);
        this.access = access;
        IItemHandler handler = controller == null ? new ItemStackHandler(CUSTOM_SLOTS) : controller.getMenuItems();

        addSlot(new SlotItemHandler(handler, SepticTankMenuLayout.handlerSlot(SepticTankMenuLayout.CONTAINER_INPUT), 80, 52));
        addSlot(new OutputOnlySlot(handler, SepticTankMenuLayout.handlerSlot(SepticTankMenuLayout.CONTAINER_OUTPUT), 80, 93));
        for (int row = 0; row < 3; row++) {
            addSlot(new SlotItemHandler(handler, SepticTankMenuLayout.handlerSlot(SepticTankMenuLayout.ITEM_INPUT_START + row), 116, 57 + row * 18));
        }
        for (int row = 0; row < 3; row++) addSlot(new OutputOnlySlot(
                handler,
                SepticTankMenuLayout.handlerSlot(SepticTankMenuLayout.ITEM_OUTPUT_START + row),
                152,
                57 + row * 18
        ));
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++)
            addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 116 + row * 18));
        for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col, 8 + col * 18, 174));

        this.data = controller == null ? new SimpleContainerData(DATA_COUNT) : new ContainerData() {
            @Override public int get(int index) {
                return switch (index) {
                    case DATA_LIQUID_AMOUNT -> controller.liquidTank.getFluidAmount();
                    case DATA_GAS_AMOUNT -> (int) controller.gasTank.getStored();
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

    public int liquidAmount() { return data.get(DATA_LIQUID_AMOUNT) & 0xFFFF; }
    public int liquidCapacity() { return SepticTankControllerBlockEntity.TANK_CAPACITY; }
    public int gasAmount() { return data.get(DATA_GAS_AMOUNT) & 0xFFFF; }
    public int gasCapacity() { return SepticTankControllerBlockEntity.TANK_CAPACITY; }
    public boolean structureValid() { return data.get(DATA_STRUCTURE_VALID) != 0; }
    public FluidStack liquidStack() { return displayStack(DATA_LIQUID_TYPE, liquidAmount()); }
    public ChemicalStack gasStack() { return displayChemicalStack(DATA_GAS_TYPE, gasAmount()); }

    private static int fluidWireValue(FluidStack stack) {
        return stack.isEmpty() ? 0 : SepticTankFluidDisplay.encodeRegistryId(BuiltInRegistries.FLUID.getId(stack.getFluid()));
    }

    private static int chemicalWireValue(ChemicalStack stack) {
        return stack.isEmpty() ? 0 : SepticTankChemicalDisplay.encodeRegistryId(
                MekanismAPI.CHEMICAL_REGISTRY.getId(stack.getChemical()));
    }

    private FluidStack displayStack(int dataIndex, int amount) {
        int registryId = SepticTankFluidDisplay.decodeRegistryId(data.get(dataIndex) & 0xFFFF);
        if (registryId < 0 || amount <= 0) return FluidStack.EMPTY;
        Fluid fluid = BuiltInRegistries.FLUID.byId(registryId);
        return new FluidStack(fluid, amount);
    }

    private ChemicalStack displayChemicalStack(int dataIndex, long amount) {
        int registryId = SepticTankChemicalDisplay.decodeRegistryId(data.get(dataIndex) & 0xFFFF);
        if (registryId < 0 || amount <= 0) return ChemicalStack.EMPTY;
        Chemical chemical = MekanismAPI.CHEMICAL_REGISTRY.byId(registryId);
        return chemical == null ? ChemicalStack.EMPTY
                : new ChemicalStack(MekanismAPI.CHEMICAL_REGISTRY.wrapAsHolder(chemical), amount);
    }

    @Override public boolean stillValid(@NotNull Player player) {
        return structureValid() && stillValid(access, player, ModBlocks.SEPTIC_TANK_CONTROLLER.get());
    }

    @Override public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        if (index < 0 || index >= slots.size()) return ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack source = slot.getItem();
        ItemStack copy = source.copy();
        if (index < CUSTOM_SLOTS) {
            if (!moveItemStackTo(source, SepticTankMenuLayout.PLAYER_START, SepticTankMenuLayout.PLAYER_END, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            boolean acceptsContainerInput = slots.get(SepticTankMenuLayout.CONTAINER_INPUT).mayPlace(source);
            boolean moved = false;
            for (SepticTankMenuLayout.SlotRange destination : SepticTankMenuLayout.playerDestinationRanges(acceptsContainerInput)) {
                if (moveItemStackTo(source, destination.startInclusive(), destination.endExclusive(), false)) {
                    moved = true;
                    if (source.isEmpty()) break;
                }
            }
            if (!moved) return ItemStack.EMPTY;
        }
        if (source.isEmpty()) slot.setByPlayer(ItemStack.EMPTY); else slot.setChanged();
        return copy;
    }
}

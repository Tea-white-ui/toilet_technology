package cn.tea.toilet.technology.gui.biogaspond;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.block.biogaspond.BiogasPondControllerBlockEntity;
import cn.tea.toilet.technology.gui.ModMenuTypes;
import cn.tea.toilet.technology.gui.septictank.OutputOnlySlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
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
    private static final int DATA_COUNT = 4;
    private final ContainerLevelAccess access;
    private final ContainerData data;

    public BiogasPondMenu(int id, Inventory inventory) { this(id, inventory, null, ContainerLevelAccess.NULL); }

    public BiogasPondMenu(int id, Inventory inventory, BiogasPondControllerBlockEntity controller, ContainerLevelAccess access) {
        super(ModMenuTypes.BIOGAS_POND_MENU.get(), id);
        this.access = access;
        IItemHandler handler = controller == null ? new ItemStackHandler(CUSTOM_SLOTS) : controller.getMenuItems();
        addSlot(new SlotItemHandler(handler, BiogasPondControllerBlockEntity.INPUT_SLOT, 116, 53));
        addSlot(new OutputOnlySlot(handler, BiogasPondControllerBlockEntity.OUTPUT_SLOT, 152, 53));
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++)
            addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 116 + row * 18));
        for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col, 8 + col * 18, 174));
        data = controller == null ? new SimpleContainerData(DATA_COUNT) : new ContainerData() {
            @Override public int get(int index) {
                return switch (index) {
                    case DATA_LIQUID_AMOUNT -> controller.liquidTank.getFluidAmount();
                    case DATA_GAS_LOW -> (int) controller.gasTank.getStored() & 0xFFFF;
                    case DATA_GAS_HIGH -> (int) (controller.gasTank.getStored() >>> 16) & 0xFFFF;
                    case DATA_STRUCTURE_VALID -> controller.isStructureValid() ? 1 : 0;
                    default -> 0;
                };
            }
            @Override public void set(int index, int value) { }
            @Override public int getCount() { return DATA_COUNT; }
        };
        addDataSlots(data);
    }

    public int liquidAmount() { return data.get(DATA_LIQUID_AMOUNT) & 0xFFFF; }
    public long gasAmount() { return (data.get(DATA_GAS_LOW) & 0xFFFFL) | ((data.get(DATA_GAS_HIGH) & 0xFFFFL) << 16); }
    public boolean structureValid() { return data.get(DATA_STRUCTURE_VALID) != 0; }

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

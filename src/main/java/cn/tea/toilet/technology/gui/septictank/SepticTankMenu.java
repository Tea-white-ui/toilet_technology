package cn.tea.toilet.technology.gui.septictank;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.block.septictank.SepticTankControllerBlockEntity;
import cn.tea.toilet.technology.gui.ModMenuTypes;
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

public class SepticTankMenu extends AbstractContainerMenu {
    public static final int CUSTOM_SLOTS = 8;
    private final ContainerLevelAccess access;
    private final ContainerData data;

    public SepticTankMenu(int id, Inventory inventory) { this(id, inventory, null, ContainerLevelAccess.NULL); }

    public SepticTankMenu(int id, Inventory inventory, SepticTankControllerBlockEntity controller, ContainerLevelAccess access) {
        super(ModMenuTypes.SEPTIC_TANK_MENU.get(), id);
        this.access = access;
        IItemHandler handler = controller == null ? new ItemStackHandler(CUSTOM_SLOTS) : controller.items;

        addSlot(new SlotItemHandler(handler, 6, 80, 52));
        addSlot(new OutputOnlySlot(handler, 7, 80, 93));
        for (int row = 0; row < 3; row++) {
            addSlot(new SlotItemHandler(handler, row, 116, 57 + row * 18));
        }
        for (int row = 0; row < 3; row++) addSlot(new OutputOnlySlot(handler, 3 + row, 152, 57 + row * 18));
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++)
            addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 116 + row * 18));
        for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col, 8 + col * 18, 174));

        this.data = controller == null ? new SimpleContainerData(3) : new ContainerData() {
            @Override public int get(int index) {
                return switch (index) {
                    case 0 -> controller.liquidTank.getFluidAmount();
                    case 1 -> controller.gasTank.getFluidAmount();
                    case 2 -> controller.isStructureValid() ? 1 : 0;
                    default -> 0;
                };
            }
            @Override public void set(int index, int value) { }
            @Override public int getCount() { return 3; }
        };
        addDataSlots(data);
    }

    public int liquidAmount() { return data.get(0) & 0xFFFF; }
    public int liquidCapacity() { return SepticTankControllerBlockEntity.TANK_CAPACITY; }
    public int gasAmount() { return data.get(1) & 0xFFFF; }
    public int gasCapacity() { return SepticTankControllerBlockEntity.TANK_CAPACITY; }
    public boolean structureValid() { return data.get(2) != 0; }

    @Override public boolean stillValid(@NotNull Player player) {
        return structureValid() && stillValid(access, player, ModBlocks.SEPTIC_TANK_CONTROLLER.get());
    }

    @Override public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack source = slot.getItem();
        ItemStack copy = source.copy();
        int playerStart = CUSTOM_SLOTS;
        int playerEnd = playerStart + 36;
        if (index < CUSTOM_SLOTS) {
            if (!moveItemStackTo(source, playerStart, playerEnd, true)) return ItemStack.EMPTY;
        } else {
            boolean moved = FluidUtilHelper.isFluidContainer(source)
                    ? moveItemStackTo(source, 0, 1, false)
                    : moveItemStackTo(source, 2, 5, false);
            if (!moved) return ItemStack.EMPTY;
        }
        if (source.isEmpty()) slot.setByPlayer(ItemStack.EMPTY); else slot.setChanged();
        return copy;
    }
}

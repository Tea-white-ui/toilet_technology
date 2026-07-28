package cn.tea.toilet.technology.gui.gasmeltingfurnace;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.block.gasmeltingfurnace.GasMeltingFurnaceBlockEntity;
import cn.tea.toilet.technology.gas.Gas;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.gas.GasStack;
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

public final class GasMeltingFurnaceMenu extends AbstractContainerMenu {
    private static final int CUSTOM_SLOT_COUNT = 2;
    private static final int DATA_GAS_AMOUNT = 0;
    private static final int DATA_GAS_TYPE = 1;
    private static final int DATA_PROCESSING_PROGRESS = 2;
    private static final int DATA_PROCESSING_TIME = 3;
    private static final int DATA_COUNT = 4;

    private final ContainerLevelAccess access;
    private final ContainerData data;

    public GasMeltingFurnaceMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, null, ContainerLevelAccess.NULL);
    }

    public GasMeltingFurnaceMenu(int containerId, Inventory inventory, GasMeltingFurnaceBlockEntity blockEntity,
                                 ContainerLevelAccess access) {
        super(ModMenuTypes.GAS_MELTING_FURNACE_MENU.get(), containerId);
        this.access = access;
        IItemHandler handler = blockEntity == null ? new ItemStackHandler(CUSTOM_SLOT_COUNT) : blockEntity.getMenuItems();
        addSlot(new SlotItemHandler(handler, GasMeltingFurnaceBlockEntity.INPUT_SLOT, 62, 26));
        addSlot(new GasMeltingFurnaceOutputSlot(handler, GasMeltingFurnaceBlockEntity.OUTPUT_SLOT, 116, 25));
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(inventory, column, 8 + column * 18, 142));
        }
        data = blockEntity == null ? new SimpleContainerData(DATA_COUNT) : new ContainerData() {
            @Override
            public int get(int index) {
                GasStack stack = blockEntity.getGasHandler(null).getGasInTank(0);
                return switch (index) {
                    case DATA_GAS_AMOUNT -> (int) stack.getAmount();
                    case DATA_GAS_TYPE -> stack.isEmpty() ? 0 : GasRegistry.id(stack.getGas()) + 1;
                    case DATA_PROCESSING_PROGRESS -> blockEntity.getProcessingProgress();
                    case DATA_PROCESSING_TIME -> blockEntity.getProcessingTime();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) { }

            @Override
            public int getCount() {
                return DATA_COUNT;
            }
        };
        addDataSlots(data);
    }

    public GasStack gasStack() {
        int gasIndex = data.get(DATA_GAS_TYPE) - 1;
        Gas gas = GasRegistry.byId(gasIndex);
        int amount = data.get(DATA_GAS_AMOUNT);
        return gas == null || amount <= 0 ? GasStack.EMPTY : new GasStack(gas, amount);
    }

    public boolean isProcessing() {
        return data.get(DATA_PROCESSING_PROGRESS) > 0 && data.get(DATA_PROCESSING_TIME) > 0;
    }

    public int getProcessingProgressScaled(int width) {
        int progress = data.get(DATA_PROCESSING_PROGRESS);
        int processingTime = data.get(DATA_PROCESSING_TIME);
        if (progress <= 0 || processingTime <= 0) return 0;
        return Math.clamp((int) ((long) progress * width / processingTime), 0, width);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(access, player, ModBlocks.GAS_MELTING_FURNACE.get());
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        if (index < 0 || index >= slots.size() || !slots.get(index).hasItem()) return ItemStack.EMPTY;
        Slot slot = slots.get(index);
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        if (index < CUSTOM_SLOT_COUNT) {
            if (!moveItemStackTo(stack, CUSTOM_SLOT_COUNT, slots.size(), true)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(stack, 0, 1, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY); else slot.setChanged();
        return copy;
    }
}

package cn.tea.toilet.technology.gui;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.block.drying.DryingBoxBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * 干燥箱菜单
 * 左边16格输入 + 右边16格输出，中间箭头
 */
public class DryingBoxMenu extends AbstractContainerMenu {

    public static final int INPUT_SLOTS = 16;
    public static final int OUTPUT_SLOTS = 16;
    public static final int TOTAL_CUSTOM_SLOTS = INPUT_SLOTS + OUTPUT_SLOTS;

    private final ContainerLevelAccess access;
    private final DryingBoxBlockEntity blockEntity;

    // 客户端构造器（用于网络同步）
    public DryingBoxMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, null, ContainerLevelAccess.NULL);
    }

    // 服务端构造器
    public DryingBoxMenu(int containerId, Inventory playerInventory, DryingBoxBlockEntity blockEntity, ContainerLevelAccess access) {
        super(ModMenuTypes.DRYING_BOX_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.access = access;

        // 获取物品处理器：服务端用真实处理器，客户端用临时占位处理器（保证槽位数量一致）
        IItemHandler inputHandler = blockEntity != null ? blockEntity.inputHandler : new ItemStackHandler(INPUT_SLOTS);
        IItemHandler outputHandler = blockEntity != null ? blockEntity.outputHandler : new ItemStackHandler(OUTPUT_SLOTS);

        // 输入槽位：左边 4x4 网格
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                int slotIndex = row * 4 + col;
                this.addSlot(new SlotItemHandler(inputHandler, slotIndex,
                        8 + col * 18, 13 + row * 18));
            }
        }

        // 输出槽位：右边 4x4 网格
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                int slotIndex = row * 4 + col;
                this.addSlot(new DryingBoxOutputSlot(outputHandler, slotIndex,
                        98 + col * 18, 13 + row * 18));
            }
        }

        // 玩家背包（3行 × 9列）
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9,
                        8 + col * 18, 95 + row * 18));
            }
        }

        // 玩家快捷栏
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col,
                    8 + col * 18, 153));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stackInSlot = slot.getItem();
        ItemStack copy = stackInSlot.copy();

        int playerInventoryStart = TOTAL_CUSTOM_SLOTS;
        int playerInventoryEnd = playerInventoryStart + 27;
        int hotbarStart = playerInventoryEnd;
        int hotbarEnd = hotbarStart + 9;

        if (index < INPUT_SLOTS) {
            // 从输入槽位移到玩家背包（主背包 + 快捷栏）
            if (!this.moveItemStackTo(stackInSlot, playerInventoryStart, hotbarEnd, true)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TOTAL_CUSTOM_SLOTS) {
            // 从输出槽位移到玩家背包（主背包 + 快捷栏）
            if (!this.moveItemStackTo(stackInSlot, playerInventoryStart, hotbarEnd, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            // 从玩家背包点击：优先移到输入槽位，然后在玩家背包内部移动
            if (!this.moveItemStackTo(stackInSlot, 0, INPUT_SLOTS, false)) {
                // 输入槽位放不下，在玩家背包内部移动
                if (index < playerInventoryEnd) {
                    // 从主背包 → 快捷栏
                    if (!this.moveItemStackTo(stackInSlot, hotbarStart, hotbarEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    // 从快捷栏 → 主背包
                    if (!this.moveItemStackTo(stackInSlot, playerInventoryStart, playerInventoryEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (stackInSlot.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return copy;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, ModBlocks.DRYING_BOX.get());
    }

    public DryingBoxBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
package cn.tea.toilet.technology.gui;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * 干燥箱输出槽位
 * 玩家只能从中取出物品，不能放入物品
 */
public class DryingBoxOutputSlot extends SlotItemHandler {

    public DryingBoxOutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }
}

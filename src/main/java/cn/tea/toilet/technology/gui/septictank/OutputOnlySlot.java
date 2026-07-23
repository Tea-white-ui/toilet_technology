package cn.tea.toilet.technology.gui.septictank;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class OutputOnlySlot extends SlotItemHandler {
    public OutputOnlySlot(IItemHandler handler, int index, int x, int y) { super(handler, index, x, y); }
    @Override public boolean mayPlace(@NotNull ItemStack stack) { return false; }
}

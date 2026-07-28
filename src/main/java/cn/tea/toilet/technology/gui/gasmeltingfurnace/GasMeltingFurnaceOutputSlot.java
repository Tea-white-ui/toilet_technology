package cn.tea.toilet.technology.gui.gasmeltingfurnace;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

final class GasMeltingFurnaceOutputSlot extends SlotItemHandler {
    GasMeltingFurnaceOutputSlot(IItemHandler handler, int index, int x, int y) {
        super(handler, index, x, y);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }
}

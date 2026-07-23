package cn.tea.toilet.technology.gui.septictank;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidUtil;

final class FluidUtilHelper {
    private FluidUtilHelper() { }
    static boolean isFluidContainer(ItemStack stack) { return FluidUtil.getFluidHandler(stack).isPresent(); }
}

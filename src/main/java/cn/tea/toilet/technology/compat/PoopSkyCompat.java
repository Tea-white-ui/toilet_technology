package cn.tea.toilet.technology.compat;

import cn.tea.toilet.technology.ModTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Defines the PoopSky materials accepted as local feces inputs without linking against PoopSky classes.
 * External fluid stacks retain their original identity while equivalent machines process them as feces liquid.
 */
public final class PoopSkyCompat {
    private PoopSkyCompat() {
    }

    public static boolean isFecesItem(ItemStack stack) {
        return !stack.isEmpty() && (stack.is(ModTags.Items.FECES)
                || PoopSkyCompatIds.isFecesItemId(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString()));
    }

    public static boolean isFecesLiquid(FluidStack stack) {
        return !stack.isEmpty()
                && PoopSkyCompatIds.isFecesLiquidId(BuiltInRegistries.FLUID.getKey(stack.getFluid()).toString());
    }
}

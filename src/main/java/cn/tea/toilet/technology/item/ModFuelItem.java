package cn.tea.toilet.technology.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModFuelItem extends Item {
    private int burnTime = 0;

    @Override
    public int getBurnTime(@NotNull ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.burnTime;
    }

    public ModFuelItem(Properties properties,int burnTime) {
        super(properties);
        this.burnTime = burnTime;
    }

}

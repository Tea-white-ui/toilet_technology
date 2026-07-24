package cn.tea.toilet.technology.compat.jei;

import cn.tea.toilet.technology.gas.GasStack;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class GasStackJeiIngredientHelper implements IIngredientHelper<GasStack> {
    @Override
    public IIngredientType<GasStack> getIngredientType() {
        return GasStackJeiIngredient.TYPE;
    }

    @Override
    public String getDisplayName(GasStack ingredient) {
        return ingredient.getTextComponent().getString();
    }

    @Override
    @SuppressWarnings("removal")
    public String getUniqueId(GasStack ingredient, UidContext context) {
        return ingredient.getGas().id().toString();
    }

    @Override
    public Object getUid(GasStack ingredient, UidContext context) {
        return ingredient.getGas().id();
    }

    @Override
    public long getAmount(GasStack ingredient) {
        return ingredient.getAmount();
    }

    @Override
    public GasStack copyWithAmount(GasStack ingredient, long amount) {
        return ingredient.copyWithAmount(amount);
    }

    @Override
    public Iterable<Integer> getColors(GasStack ingredient) {
        return List.of(ingredient.getGas().tint());
    }

    @Override
    public ResourceLocation getResourceLocation(GasStack ingredient) {
        return ingredient.getGas().id();
    }

    @Override
    public GasStack copyIngredient(GasStack ingredient) {
        return ingredient.copyWithAmount(ingredient.getAmount());
    }

    @Override
    public boolean isValidIngredient(GasStack ingredient) {
        return !ingredient.isEmpty();
    }

    @Override
    public String getErrorInfo(@Nullable GasStack ingredient) {
        return ingredient == null ? "null gas stack" : ingredient.toString();
    }
}

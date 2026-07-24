package cn.tea.toilet.technology.compat.jei;

import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.gas.GasStack;
import com.mojang.serialization.Codec;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.resources.ResourceLocation;

public final class GasStackJeiIngredient {
    public static final IIngredientType<GasStack> TYPE = () -> GasStack.class;
    public static final Codec<GasStack> CODEC = Codec.STRING.xmap(
            id -> new GasStack(GasRegistry.byId(ResourceLocation.parse(id)), 1),
            stack -> stack.getGas().id().toString()
    );

    private GasStackJeiIngredient() {
    }
}

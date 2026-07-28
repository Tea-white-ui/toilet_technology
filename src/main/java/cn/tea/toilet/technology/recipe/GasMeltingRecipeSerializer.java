package cn.tea.toilet.technology.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

/** 燃气熔炼炉配方的 JSON 与网络序列化器。 */
public final class GasMeltingRecipeSerializer implements RecipeSerializer<GasMeltingRecipe> {
    @Override
    public MapCodec<GasMeltingRecipe> codec() {
        return GasMeltingRecipe.CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, GasMeltingRecipe> streamCodec() {
        return GasMeltingRecipe.STREAM_CODEC;
    }
}

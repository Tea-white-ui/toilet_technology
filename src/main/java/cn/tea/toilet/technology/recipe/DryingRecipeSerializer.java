package cn.tea.toilet.technology.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;

/**
 * 干燥配方序列化器
 * 负责配方的JSON读取/写入和网络传输
 */
public class DryingRecipeSerializer implements RecipeSerializer<DryingRecipe> {

    // JSON编解码器
    @Override
    public MapCodec<DryingRecipe> codec() {
        return DryingRecipe.CODEC;
    }

    // 网络流编解码器
    @Override
    public StreamCodec<RegistryFriendlyByteBuf, DryingRecipe> streamCodec() {
        return DryingRecipe.STREAM_CODEC;
    }
}
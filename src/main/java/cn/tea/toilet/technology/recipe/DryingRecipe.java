package cn.tea.toilet.technology.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

/**
 * 干燥配方类 - 定义物品在干燥架上的转化规则
 * 每个配方包含：输入物品、输出物品、干燥所需时间(tick)
 */
public class DryingRecipe implements Recipe<SingleRecipeInput> {

    // 配方序列化器，用于JSON读取/写入
    public static final MapCodec<DryingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(DryingRecipe::getInputIngredient),
                    ItemStack.CODEC.fieldOf("output").forGetter(recipe -> recipe.output),
                    com.mojang.serialization.Codec.intRange(0, Integer.MAX_VALUE).fieldOf("drying_time").forGetter(recipe -> recipe.dryingTime)
            ).apply(instance, DryingRecipe::new));

    // 网络传输编解码器
    public static final StreamCodec<RegistryFriendlyByteBuf, DryingRecipe> STREAM_CODEC = StreamCodec.of(
            DryingRecipe::toNetwork,
            DryingRecipe::fromNetwork
    );

    // 输入物品（支持标签匹配）
    private final Ingredient input;
    // 输出物品
    private final ItemStack output;
    // 干燥所需时间（单位：tick，20 tick = 1秒）
    private final int dryingTime;

    public DryingRecipe(Ingredient input, ItemStack output, int dryingTime) {
        this.input = input;
        this.output = output;
        this.dryingTime = dryingTime;
    }

    // ==================== Getter 方法 ====================

    public Ingredient getInputIngredient() {
        return input;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output;
    }

    public int getDryingTime() {
        return dryingTime;
    }

    // ==================== Recipe 接口实现 ====================

    /**
     * 检查输入物品是否匹配此配方
     */
    @Override
    public boolean matches(SingleRecipeInput inv, Level level) {
        return input.test(inv.item());
    }

    /**
     * 执行配方，返回转化后的物品
     */
    @Override
    public ItemStack assemble(SingleRecipeInput inv, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.DRYING_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.DRYING.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(input);
        return list;
    }

    // ==================== 网络序列化 ====================

    private static void toNetwork(RegistryFriendlyByteBuf buffer, DryingRecipe recipe) {
        // 使用 Codec 将 Ingredient 编码为 NBT，然后写入缓冲区
        var nbt = Ingredient.CODEC_NONEMPTY.encodeStart(NbtOps.INSTANCE, recipe.input).result().orElseThrow();
        buffer.writeNbt(nbt);
        ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
        buffer.writeVarInt(recipe.dryingTime);
    }

    private static DryingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        // 从缓冲区读取 NBT，然后使用 Codec 解码 Ingredient
        var nbt = buffer.readNbt();
        Ingredient input = Ingredient.CODEC_NONEMPTY.parse(NbtOps.INSTANCE, nbt).result().orElseThrow();
        ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
        int dryingTime = buffer.readVarInt();
        return new DryingRecipe(input, output, dryingTime);
    }

    /**
     * 便捷方法：检查物品堆栈是否匹配此配方的输入
     */
    public boolean matches(ItemStack stack) {
        return input.test(stack);
    }
}
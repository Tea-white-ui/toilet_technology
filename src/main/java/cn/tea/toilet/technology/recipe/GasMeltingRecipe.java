package cn.tea.toilet.technology.recipe;

import cn.tea.toilet.technology.gas.Gas;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.gas.GasStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

/**
 * 燃气熔炼炉配方：消耗指定气体并将一个输入物品转化为输出物品。
 * 气体以稳定的注册表 ID 保存，避免核心机器依赖可选模组的气体类型。
 */
public final class GasMeltingRecipe implements Recipe<SingleRecipeInput> {
    public static final MapCodec<GasMeltingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(GasMeltingRecipe::input),
            ItemStack.CODEC.fieldOf("output").forGetter(GasMeltingRecipe::output),
            ResourceLocation.CODEC.fieldOf("gas").forGetter(recipe -> recipe.gas.id()),
            Codec.LONG.fieldOf("gas_amount").forGetter(GasMeltingRecipe::gasAmount),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("processing_time").forGetter(GasMeltingRecipe::processingTime)
    ).apply(instance, GasMeltingRecipe::fromSerialized));

    public static final StreamCodec<RegistryFriendlyByteBuf, GasMeltingRecipe> STREAM_CODEC = StreamCodec.of(
            GasMeltingRecipe::toNetwork, GasMeltingRecipe::fromNetwork);

    private final Ingredient input;
    private final ItemStack output;
    private final Gas gas;
    private final long gasAmount;
    private final int processingTime;

    public GasMeltingRecipe(Ingredient input, ItemStack output, Gas gas, long gasAmount, int processingTime) {
        if (gasAmount <= 0) throw new IllegalArgumentException("Gas amount must be positive");
        this.input = input;
        this.output = output.copy();
        this.gas = gas;
        this.gasAmount = gasAmount;
        this.processingTime = processingTime;
    }

    private static GasMeltingRecipe fromSerialized(Ingredient input, ItemStack output, ResourceLocation gasId,
                                                    long gasAmount, int processingTime) {
        Gas gas = GasRegistry.byId(gasId);
        if (gas == null) throw new IllegalArgumentException("Unknown gas in gas melting recipe: " + gasId);
        return new GasMeltingRecipe(input, output, gas, gasAmount, processingTime);
    }

    public Ingredient input() { return input; }
    public ItemStack output() { return output.copy(); }
    public Gas gas() { return gas; }
    public long gasAmount() { return gasAmount; }
    public int processingTime() { return processingTime; }

    public boolean hasRequiredGas(GasStack stack) {
        return stack.is(gas) && stack.amount() >= gasAmount;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return this.input.test(input.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        return output();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.GAS_MELTING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.GAS_MELTING.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(input);
        return ingredients;
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, GasMeltingRecipe recipe) {
        buffer.writeNbt(Ingredient.CODEC_NONEMPTY.encodeStart(NbtOps.INSTANCE, recipe.input).result().orElseThrow());
        ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
        buffer.writeResourceLocation(recipe.gas.id());
        buffer.writeVarLong(recipe.gasAmount);
        buffer.writeVarInt(recipe.processingTime);
    }

    private static GasMeltingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        Ingredient input = Ingredient.CODEC_NONEMPTY.parse(NbtOps.INSTANCE, buffer.readNbt()).result().orElseThrow();
        ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
        Gas gas = GasRegistry.byId(buffer.readResourceLocation());
        if (gas == null) throw new IllegalArgumentException("Unknown gas in gas melting recipe network payload");
        return new GasMeltingRecipe(input, output, gas, buffer.readVarLong(), buffer.readVarInt());
    }
}

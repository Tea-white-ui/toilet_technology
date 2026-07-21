package cn.tea.toilet.technology.compat.jei;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

/**
 * JEI配方类别定义接口 - 将每个配方类别的自描述信息内聚到一个对象中
 *
 * 每个实现类负责定义：
 * - getMinecraftRecipeType() — 对应的Minecraft配方类型（核心数据源）
 * - getRecipeType() — JEI配方类型标识（基于Minecraft配方类型UID自动构建）
 * - createCategory() — JEI可视化布局实例
 * - getCatalysts() — 催化剂列表
 * - collectRecipes() — 配方收集（提供默认实现，基于getMinecraftRecipeType自动收集）
 *
 * 通过此抽象，新增配方类型时无需修改Plugin主类，只需新增一个Definition实现并注册即可。
 *
 * @param <T> 配方类型，必须继承自Recipe
 */
public interface JeiCategoryDefinition<T extends Recipe<?>> {

    /**
     * 获取对应的Minecraft配方类型（核心数据源）
     * 所有JEI相关的类型标识和配方收集都基于此方法派生
     */
    net.minecraft.world.item.crafting.RecipeType<T> getMinecraftRecipeType();

    /**
     * 获取配方类的Class对象
     * 用于构建JEI RecipeType
     */
    Class<T> getRecipeClass();

    /**
     * 获取JEI配方类型标识
     * 默认实现：基于Minecraft配方类型的注册表键自动构建，无需子类手动指定
     */
    default RecipeType<T> getRecipeType() {
        ResourceLocation uid = BuiltInRegistries.RECIPE_TYPE.getKey(getMinecraftRecipeType());
        return RecipeType.create(uid.getNamespace(), uid.getPath(), getRecipeClass());
    }

    /**
     * 创建JEI配方类别实例（负责可视化布局）
     *
     * @param guiHelper JEI提供的GUI辅助工具
     * @return 配方类别实例
     */
    IRecipeCategory<T> createCategory(IGuiHelper guiHelper);

    /**
     * 从游戏配方管理器中收集所有该类型的配方
     * 默认实现：基于getMinecraftRecipeType自动收集并解包RecipeHolder
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    default List<T> collectRecipes(RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor((net.minecraft.world.item.crafting.RecipeType) getMinecraftRecipeType()).stream()
                .map(holder -> (T) ((RecipeHolder) holder).value())
                .toList();
    }

    /**
     * 获取该配方的催化剂列表（执行该配方的方块/物品）
     *
     * @return 催化剂物品列表
     */
    List<ItemStack> getCatalysts();
}
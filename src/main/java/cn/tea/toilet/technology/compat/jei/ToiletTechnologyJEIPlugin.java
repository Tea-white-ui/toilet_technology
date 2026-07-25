
package cn.tea.toilet.technology.compat.jei;
import cn.tea.toilet.technology.ModConstants;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.item.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * JEI插件主类 -负责注册JEI的所有集成内容 *
 * 这个类是JEI集成的入口点，通过@JeiPlugin注解被JEI自动发现 *负责遍历所有JeiCategoryDefinition，统一注册配方类别、配方实例和配方催化剂 *
 * 新增配方类型时，只需新增一个JeiCategoryDefinition实现并添加到CATEGORY_DEFINITIONS列表，
 * 无需修改本类的任何方法。
 */

@JeiPlugin
public class ToiletTechnologyJEIPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(
            ModConstants.MOD_ID, "jei_plugin"
    );

    /**
     * 所有JEI配方类别定义列表 *新增配方类型时，只需在此列表中添加对应的Definition实现
     */
    private static final List<JeiCategoryDefinition<?>> CATEGORY_DEFINITIONS = List.of(
            new DryingCategoryDefinition()
    );

    @NotNull
    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                BiogasProductionJeiCategory.septicTank(registration.getJeiHelpers().getGuiHelper()),
                BiogasProductionJeiCategory.biogasPond(registration.getJeiHelpers().getGuiHelper())
        );
        for (JeiCategoryDefinition<?> definition : CATEGORY_DEFINITIONS) {
            registerCategory(definition, registration);
        }
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        //手动向 JEI注册燃料物品信息（不依赖 level，必须在 null检查之前调用）
        registerFuelInfo(registration);
        registration.addRecipes(BiogasProductionJeiCategory.SEPTIC_TANK_RECIPE_TYPE,
                BiogasProductionJeiRecipes.septicTankRecipes());
        registration.addRecipes(BiogasProductionJeiCategory.BIOGAS_POND_RECIPE_TYPE,
                BiogasProductionJeiRecipes.biogasPondRecipes());

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        RecipeManager recipeManager = minecraft.level.getRecipeManager();

        for (JeiCategoryDefinition<?> definition : CATEGORY_DEFINITIONS) {
            registerRecipes(definition, recipeManager, registration);
        }
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SEPTIC_TANK_CONTROLLER.get()),
                BiogasProductionJeiCategory.SEPTIC_TANK_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BIOGAS_POND_CONTROLLER.get()),
                BiogasProductionJeiCategory.BIOGAS_POND_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BIOGAS_GENERATOR.get()),
                BiogasProductionJeiCategory.BIOGAS_POND_RECIPE_TYPE);
        for (JeiCategoryDefinition<?> definition : CATEGORY_DEFINITIONS) {
            registerCatalysts(definition, registration);
        }
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        registration.register(
                GasStackJeiIngredient.TYPE,
                List.of(new GasStack(GasRegistry.BIOGAS, 1)),
                new GasStackJeiIngredientHelper(),
                new GasStackJeiIngredientRenderer(),
                GasStackJeiIngredient.CODEC
        );
    }

    /**
     * 向 JEI注册模组的燃料物品，使 JEI的燃料界面能识别干粪等可作为燃料的物品
     */
    private void registerFuelInfo(IRecipeRegistration registration) {
        registration.getIngredientManager().addIngredientsAtRuntime(
                VanillaTypes.ITEM_STACK,
                List.of(
                        new ItemStack(ModItems.DRIED_FECES.get()),
                        new ItemStack(ModItems.DRIED_FECES_BLOCK_ITEM.get())
                )
        );
    }

    /**
     * 注册单个配方类别（通过泛型捕获解决通配符类型推断问题）
     */
    private static <T extends Recipe<?>> void registerCategory(
            JeiCategoryDefinition<T> definition,
            IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                definition.createCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    /**
     * 注册单个配方类型的所有配方实例（通过泛型捕获解决通配符类型推断问题）
     */
    private static <T extends Recipe<?>> void registerRecipes(
            JeiCategoryDefinition<T> definition,
            RecipeManager recipeManager,
            IRecipeRegistration registration) {
        List<T> recipes = definition.collectRecipes(recipeManager);
        registration.addRecipes(definition.getRecipeType(), recipes);
    }

    /**
     * 注册单个配方类型的催化剂（通过泛型捕获解决通配符类型推断问题）
     */
    private static <T extends Recipe<?>> void registerCatalysts(
            JeiCategoryDefinition<T> definition,
            IRecipeCatalystRegistration registration) {
        for (var catalyst : definition.getCatalysts()) {
            registration.addRecipeCatalyst(catalyst, definition.getRecipeType());
        }
    }
}
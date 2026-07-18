package tea.toilet.technology.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import tea.toilet.technology.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {


    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        /*
         * ===== 有序合成配方（ShapedRecipeBuilder） =====
         * 要求材料在工作台中按固定位置摆放
         *
         * ShapedRecipeBuilder.shaped(
         *     RecipeCategory category,  // 配方分类，决定在配方书中归入哪一类
         *                               // 常用值：BUILDING_BLOCKS / MISC / TOOLS / COMBAT 等
         *     ItemLike result           // 合成输出的物品
         * )
         * .pattern(String row)          // 定义合成网格的一行，最多3行（3x3网格）
         *                               // 每个字符为一个占位符，空格表示空槽位
         *                               // 示例："AB" 表示第一格放A、第二格放B、第三格为空
         * .define(char key, ItemLike ingredient)  // 将占位符字符映射到具体物品
         *                                         // 每个 pattern 中出现的字符都必须 define
         * .unlockedBy(String criterionName, Criterion<?> criterion)  // 解锁条件
         *     // criterionName: 条件名（自定义字符串）
         *     // criterion: 触发条件，常用 has(物品) 表示"获得该物品时解锁"
         * .save(RecipeOutput exporter)  // 必须调用！将配方写入JSON文件
         *     // 默认保存路径为 namespace:recipe/item_name，无需手动指定
         *
         * 下方示例：2x2 的 D 排列 → 1个粪便块（4个粪便合成）
         */

        /*
         * ===== 无序合成配方（ShapelessRecipeBuilder） =====
         * 材料任意摆放即可合成，不要求位置
         *
         * ShapelessRecipeBuilder.shapeless(
         *     RecipeCategory category,  // 配方分类，同上
         *     ItemLike result           // 合成输出的物品
         * )
         * .requires(ItemLike ingredient)  // 添加一个所需材料，可多次调用添加多个
         *                                 // 每次调用增加1个该物品的需求
         * .requires(TagKey<Item> tag)     // 也可用物品标签作为材料（如 minecraft:planks）
         * .unlockedBy(String criterionName, Criterion<?> criterion)  // 解锁条件，同有序配方
         * .save(RecipeOutput exporter)    // 必须调用！保存配方JSON
         *
         * 下方示例：2个粪便 + 1个煤炭 → 1个粪便块（无序摆放）
         */


        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.FECES_BLOCK)
                .pattern("DD")
                .pattern("DD")
                .define('D',ModItems.FECES)
                .unlockedBy("has_feces", has(ModItems.FECES))
                .save(exporter);
    }
}

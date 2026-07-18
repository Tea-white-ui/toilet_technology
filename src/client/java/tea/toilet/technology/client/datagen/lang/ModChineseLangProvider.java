package tea.toilet.technology.client.datagen.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import tea.toilet.technology.block.ModBlocks;
import tea.toilet.technology.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModChineseLangProvider extends FabricLanguageProvider {
	public ModChineseLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(dataOutput, "zh_cn", registryLookup);
	}

	@Override
	public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder translationBuilder) {
		// === 物品 ===
		translationBuilder.add(ModItems.FECES, "粪便");
		// === 建筑方块 ===
		translationBuilder.add(ModBlocks.FECES_BLOCK, "粪便块");
		// === 功能方块 ===

		// 创造物品栏标签
		translationBuilder.add("itemGroup.toilet_technology", "厕所技艺");
	}
}
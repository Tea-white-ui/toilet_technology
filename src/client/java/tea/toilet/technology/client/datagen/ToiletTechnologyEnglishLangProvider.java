package tea.toilet.technology.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import tea.toilet.technology.block.ModBlocks;
import tea.toilet.technology.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ToiletTechnologyEnglishLangProvider extends FabricLanguageProvider {
	public ToiletTechnologyEnglishLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(dataOutput, "en_us", registryLookup);
	}

	@Override
	public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder translationBuilder) {
		// === Items ===
		translationBuilder.add(ModItems.FECES, "Feces");
		// === Blocks ===
		translationBuilder.add(ModBlocks.FECES_BLOCK, "Feces Block");
	}
}
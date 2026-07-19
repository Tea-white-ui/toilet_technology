package tea.toilet.technology.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.Nullable;
import tea.toilet.technology.ModTags;
import tea.toilet.technology.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends FabricTagProvider.ItemTagProvider {


	public ModItemTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable BlockTagProvider blockTagProvider) {
		super(output, completableFuture, blockTagProvider);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {

		// === 原材料 ===
		getOrCreateTagBuilder(ModTags.Items.FECES)
				.add(ModItems.FECES)
				.add(ModItems.DRIED_FECES);
		// === 标签嵌套 ===
		getOrCreateTagBuilder(ModTags.Items.MATERIAL)
				.addTag(ModTags.Items.FECES);

		// === 建筑方块物品 ===
		copy(ModTags.Blocks.BUILDING_BLOCKS, ModTags.Items.BUILDING_BLOCK);
		// === 厕所方块物品 ===
		copy(ModTags.Blocks.TOILETS, ModTags.Items.TOILET);
	}
}